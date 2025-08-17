package msu.msuteam.onlylaststand.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.network.SyncLearnedSpellsPacket;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class MagicCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("msu")
                .then(Commands.literal("magic")
                        .then(Commands.literal("reset")
                                .then(Commands.argument("spell_tag", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("fire_spells");
                                            builder.suggest("water_spells");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            String tag = StringArgumentType.getString(context, "spell_tag");
                                            PlayerLearnedSpells learnedSpells = player.getData(ModAttachments.PLAYER_LEARNED_SPELLS);

                                            TagKey<Item> spellTag = (tag.equals("fire_spells")) ? ModTags.Items.FIRE_SPELLS : ModTags.Items.WATER_SPELLS;

                                            List<Holder<Item>> allSpells = BuiltInRegistries.ITEM.getTag(spellTag)
                                                    .map(t -> t.stream().toList()).orElse(List.of());

                                            for(Holder<Item> spellHolder : allSpells) {
                                                learnedSpells.forgetSpell(spellHolder.value());
                                            }

                                            PacketDistributor.sendToPlayer(player, new SyncLearnedSpellsPacket(learnedSpells.serializeNBT(player.registryAccess())));
                                            context.getSource().sendSuccess(() -> Component.literal("Все заклинания из категории " + tag + " были забыты."), false);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}