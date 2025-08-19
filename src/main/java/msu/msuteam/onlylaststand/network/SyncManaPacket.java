package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncManaPacket(float currentMana, float maxMana) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "sync_mana");
    public static final Type<SyncManaPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncManaPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncManaPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncManaPacket(buf.readFloat(), buf.readFloat());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncManaPacket packet) {
            buf.writeFloat(packet.currentMana);
            buf.writeFloat(packet.maxMana);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncManaPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                PlayerMana mana = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_MANA);
                // Сначала максимум, потом текущее значение, чтобы клампы были корректными
                mana.setMaxMana(packet.maxMana);
                mana.setCurrentMana(packet.currentMana);
            }
        });
    }
}