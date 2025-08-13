package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.client.SkillsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncSkillsDataPacket(CompoundTag skillsNbt) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "sync_skills_data");
    public static final Type<SyncSkillsDataPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSkillsDataPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncSkillsDataPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncSkillsDataPacket(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncSkillsDataPacket packet) {
            buf.writeNbt(packet.skillsNbt);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncSkillsDataPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof SkillsScreen skillsScreen) {
                skillsScreen.updateSkillsData(packet.skillsNbt);
            }
        });
    }
}