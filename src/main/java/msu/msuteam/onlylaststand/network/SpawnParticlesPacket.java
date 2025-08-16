package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// ИСПРАВЛЕНО: Переименован параметр 'type' в 'particleType', чтобы избежать конфликта
public record SpawnParticlesPacket(int particleType, double x, double y, double z, double xd, double yd, double zd) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "spawn_particles");
    public static final Type<SpawnParticlesPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnParticlesPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SpawnParticlesPacket decode(RegistryFriendlyByteBuf buf) {
            return new SpawnParticlesPacket(buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SpawnParticlesPacket packet) {
            buf.writeVarInt(packet.particleType);
            buf.writeDouble(packet.x);
            buf.writeDouble(packet.y);
            buf.writeDouble(packet.z);
            buf.writeDouble(packet.xd);
            buf.writeDouble(packet.yd);
            buf.writeDouble(packet.zd);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SpawnParticlesPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                if (packet.particleType == 0) { // Spark
                    Minecraft.getInstance().level.addParticle(ParticleTypes.FLAME, packet.x, packet.y, packet.z, packet.xd, packet.yd, packet.zd);
                } else if (packet.particleType == 1) { // Flame Light
                    Minecraft.getInstance().level.addParticle(ParticleTypes.FLAME, packet.x, packet.y, packet.z, packet.xd, packet.yd, packet.zd);
                }
            }
        });
    }
}