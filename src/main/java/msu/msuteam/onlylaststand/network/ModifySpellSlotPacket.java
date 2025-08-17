package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import msu.msuteam.onlylaststand.inventory.SpellMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record ModifySpellSlotPacket(int slotIndex, ResourceLocation spellId, Action action) implements CustomPacketPayload {
    public enum Action { SET, CLEAR }

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "modify_spell_slot");
    public static final Type<ModifySpellSlotPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifySpellSlotPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ModifySpellSlotPacket::slotIndex,
            ResourceLocation.STREAM_CODEC,
            ModifySpellSlotPacket::spellId,
            ByteBufCodecs.VAR_INT.map(i -> Action.values()[i], Action::ordinal),
            ModifySpellSlotPacket::action,
            ModifySpellSlotPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ModifySpellSlotPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.containerMenu instanceof SpellMenu spellMenu) {
                // ИСПРАВЛЕНО: Правильное имя инвентаря
                SpellInventory spellInventory = player.getData(ModAttachments.SPELL_INVENTORY);
                if (packet.slotIndex < 0 || packet.slotIndex >= spellInventory.getSlots()) return;

                Slot slot = spellMenu.getSlot(packet.slotIndex);

                switch (packet.action) {
                    case SET:
                        Optional<Item> spellItem = BuiltInRegistries.ITEM.getOptional(packet.spellId);
                        if (spellItem.isPresent()) {
                            ItemStack newStack = new ItemStack(spellItem.get());
                            if (slot.mayPlace(newStack)) {
                                spellInventory.setStackInSlot(packet.slotIndex, newStack);
                                player.containerMenu.setCarried(ItemStack.EMPTY);
                            }
                        }
                        break;
                    case CLEAR:
                        ItemStack takenStack = spellInventory.getStackInSlot(packet.slotIndex).copy();
                        spellInventory.setStackInSlot(packet.slotIndex, ItemStack.EMPTY);
                        player.containerMenu.setCarried(takenStack);
                        break;
                }
                PacketDistributor.sendToPlayer(player, new SyncSpellsPacket(spellInventory.serializeNBT(player.registryAccess())));
            }
        });
    }
}