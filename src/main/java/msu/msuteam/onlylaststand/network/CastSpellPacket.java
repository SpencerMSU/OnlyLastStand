package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import msu.msuteam.onlylaststand.item.spells.SpellItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastSpellPacket(int slotIndex) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "cast_spell");
    public static final Type<CastSpellPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, CastSpellPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public CastSpellPacket decode(RegistryFriendlyByteBuf buf) {
            return new CastSpellPacket(buf.readVarInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, CastSpellPacket packet) {
            buf.writeVarInt(packet.slotIndex);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final CastSpellPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                SpellInventory spellInventory = player.getData(ModAttachments.SPELL_INVENTORY);
                if (packet.slotIndex >= 0 && packet.slotIndex < spellInventory.getSlots()) {
                    ItemStack spellStack = spellInventory.getStackInSlot(packet.slotIndex);
                    if (spellStack.getItem() instanceof SpellItem spell) {
                        // Имитируем использование предмета
                        spell.use(player.level(), player, InteractionHand.MAIN_HAND);
                    }
                }
            }
        });
    }
}