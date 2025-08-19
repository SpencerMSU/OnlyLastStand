package msu.msuteam.onlylaststand.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.network.SyncSkillsDataPacket;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class SkillsCommand {

    private static final SuggestionProvider<CommandSourceStack> SKILL_SUGGESTIONS = (ctx, builder) -> {
        for (PlayerSkill skill : PlayerSkill.values()) {
            builder.suggest(skill.name());
        }
        return builder.buildFuture();
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("msu")
                .then(Commands.literal("skills").requires(src -> src.hasPermission(2))
                        // /msu skills set <skill> <level>
                        .then(Commands.literal("set")
                                .then(Commands.argument("skill", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS)
                                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 100))
                                                .executes(ctx -> {
                                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                                    String skillName = StringArgumentType.getString(ctx, "skill");
                                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                                    return setSkillLevel(player, skillName, level, ctx.getSource());
                                                })
                                        )
                                )
                        )
                        // /msu skills reset <skill>
                        .then(Commands.literal("reset")
                                .then(Commands.argument("skill", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS)
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            String skillName = StringArgumentType.getString(ctx, "skill");
                                            return resetSkill(player, skillName, ctx.getSource());
                                        })
                                )
                        )
                        // /msu skills setall <level>
                        .then(Commands.literal("setall")
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 100))
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            int level = IntegerArgumentType.getInteger(ctx, "level");
                                            return setAllSkills(player, level, ctx.getSource());
                                        })
                                )
                        )
                        // Доп. форма в стиле запроса пользователя: /msu skills <skill> set <level>
                        .then(Commands.argument("skill", StringArgumentType.word()).suggests(SKILL_SUGGESTIONS)
                                .then(Commands.literal("set")
                                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 100))
                                                .executes(ctx -> {
                                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                                    String skillName = StringArgumentType.getString(ctx, "skill");
                                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                                    return setSkillLevel(player, skillName, level, ctx.getSource());
                                                })
                                        )
                                )
                                .then(Commands.literal("reset")
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            String skillName = StringArgumentType.getString(ctx, "skill");
                                            return resetSkill(player, skillName, ctx.getSource());
                                        })
                                )
                        )
                )
        );
    }

    private static Optional<PlayerSkill> parseSkill(String input) {
        try {
            return Optional.of(PlayerSkill.valueOf(input.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private static int setSkillLevel(ServerPlayer player, String skillName, int level, CommandSourceStack source) {
        Optional<PlayerSkill> opt = parseSkill(skillName);
        if (opt.isEmpty()) {
            source.sendFailure(Component.literal("Неизвестный навык: " + skillName));
            return 0;
        }
        PlayerSkill skill = opt.get();

        PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
        int max = skills.getSkill(skill).getMaxLevel();
        int clamped = Math.max(1, Math.min(level, max));

        // Меняем через NBT, чтобы корректно применилось на сервере,
        // и одновременно обновляем unlockedSpellSlots, если меняется MAGIC.
        CompoundTag tag = skills.serializeNBT(player.registryAccess());

        CompoundTag sTag = tag.getCompound(skill.name());
        sTag.putInt("level", clamped);
        sTag.putInt("experience", 0);
        tag.put(skill.name(), sTag);

        // Пересчитать слоты заклинаний при изменении MAGIC
        if (skill == PlayerSkill.MAGIC) {
            tag.putInt("unlockedSpellSlots", 1 + (clamped / 10));
        }

        skills.deserializeNBT(player.registryAccess(), tag);
        sync(player, skills);

        source.sendSuccess(() -> Component.literal("Установлен уровень " + skill.name() + " = " + clamped), false);
        return 1;
    }

    private static int resetSkill(ServerPlayer player, String skillName, CommandSourceStack source) {
        Optional<PlayerSkill> opt = parseSkill(skillName);
        if (opt.isEmpty()) {
            source.sendFailure(Component.literal("Неизвестный навык: " + skillName));
            return 0;
        }
        PlayerSkill skill = opt.get();

        PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);

        CompoundTag tag = skills.serializeNBT(player.registryAccess());

        CompoundTag sTag = tag.getCompound(skill.name());
        sTag.putInt("level", 1);
        sTag.putInt("experience", 0);
        tag.put(skill.name(), sTag);

        if (skill == PlayerSkill.MAGIC) {
            tag.putInt("unlockedSpellSlots", 1);
        }

        skills.deserializeNBT(player.registryAccess(), tag);
        sync(player, skills);

        source.sendSuccess(() -> Component.literal("Навык " + skill.name() + " сброшен до 1 уровня."), false);
        return 1;
    }

    private static int setAllSkills(ServerPlayer player, int level, CommandSourceStack source) {
        PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
        int clamped = Math.max(1, Math.min(level, 100));

        CompoundTag tag = skills.serializeNBT(player.registryAccess());

        Arrays.stream(PlayerSkill.values()).forEach(skill -> {
            CompoundTag sTag = tag.getCompound(skill.name());
            sTag.putInt("level", clamped);
            sTag.putInt("experience", 0);
            tag.put(skill.name(), sTag);
        });

        // Пересчитать слоты заклинаний от уровня MAGIC
        tag.putInt("unlockedSpellSlots", 1 + (clamped / 10));

        skills.deserializeNBT(player.registryAccess(), tag);
        sync(player, skills);

        source.sendSuccess(() -> Component.literal("Всем навыкам установлен уровень = " + clamped), false);
        return 1;
    }

    private static void sync(ServerPlayer player, PlayerSkills skills) {
        // Отправляем клиенту актуальные данные навыков
        PacketDistributor.sendToPlayer(player, new SyncSkillsDataPacket(skills.serializeNBT(player.registryAccess())));
    }
}