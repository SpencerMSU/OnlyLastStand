package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestSkillsDataPacket() implements CustomPacketPayload {
    public static final RequestSkillsDataPacket INSTANCE = new RequestSkillsDataPacket();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "request_skills_data");
    public static final Type<RequestSkillsDataPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestSkillsDataPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RequestSkillsDataPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            PacketDistributor.sendToPlayer(player, new SyncSkillsDataPacket(skills.serializeNBT(player.registryAccess())));
        });
    }
}