package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public record TryUpgradePacket() implements CustomPacketPayload {

    public static final TryUpgradePacket INSTANCE = new TryUpgradePacket();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "try_upgrade");
    public static final Type<TryUpgradePacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, TryUpgradePacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final TryUpgradePacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.containerMenu instanceof AnvilMenu anvilMenu) {
                ItemStack accessorySlotStack = anvilMenu.getSlot(0).getItem();
                ItemStack stoneSlotStack = anvilMenu.getSlot(1).getItem();

                if (!(accessorySlotStack.getItem() instanceof AccessoryItem) || !stoneSlotStack.is(ModItems.UPGRADE_STONE.get())) return;

                int currentLevel = accessorySlotStack.getOrDefault(ModDataComponents.ACCESSORY_LEVEL, 0);
                if (currentLevel >= 10) return;

                int stonesNeeded = currentLevel + 1;
                if (stoneSlotStack.getCount() < stonesNeeded) return;

                PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
                int smithingLevel = skills.getSkill(PlayerSkill.SMITHING).getLevel();
                float discount = 1.0f - (smithingLevel * 0.0045f);
                int cost = Math.max(1, (int)((stonesNeeded * 2) * discount));

                if (player.experienceLevel < cost && !player.isCreative()) return;

                ItemStack resultStack = accessorySlotStack.copy();
                resultStack.setCount(1);

                if (!player.isCreative()) {
                    player.giveExperienceLevels(-cost);
                }
                anvilMenu.getSlot(0).remove(1);
                anvilMenu.getSlot(1).remove(stonesNeeded);

                skills.getSkill(PlayerSkill.SMITHING).addExperience(player, 25);

                double smithingBonus = smithingLevel * 0.0005;
                double successChance = Math.max(0.01, 1.0 / Math.pow(2, currentLevel)) + smithingBonus;
                boolean success = new Random().nextDouble() <= successChance;

                if (success) {
                    resultStack.set(ModDataComponents.ACCESSORY_LEVEL, currentLevel + 1);
                    player.sendSystemMessage(Component.literal("УЛУЧШЕНИЕ УСПЕШНО").withStyle(ChatFormatting.GREEN));
                    player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                } else {
                    player.sendSystemMessage(Component.literal("УЛУЧШЕНИЕ НЕУДАЧНО").withStyle(ChatFormatting.RED));
                    player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                anvilMenu.setCarried(resultStack);
            }
        });
    }
}