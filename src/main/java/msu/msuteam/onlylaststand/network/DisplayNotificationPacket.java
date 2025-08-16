package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.client.gui.NotificationManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization; // <-- ДОБАВЛЕН ИМПОРТ
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DisplayNotificationPacket(Component message) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "display_notification");
    public static final Type<DisplayNotificationPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DisplayNotificationPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public DisplayNotificationPacket decode(RegistryFriendlyByteBuf buf) {
            // ИСПРАВЛЕНО: Используем правильный кодек
            return new DisplayNotificationPacket(ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, DisplayNotificationPacket packet) {
            // ИСПРАВЛЕНО: Используем правильный кодек
            ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, packet.message);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final DisplayNotificationPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> NotificationManager.show(packet.message));
    }
}