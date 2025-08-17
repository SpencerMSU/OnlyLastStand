package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestLearnedSpellsPacket() implements CustomPacketPayload {
    public static final RequestLearnedSpellsPacket INSTANCE = new RequestLearnedSpellsPacket();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "request_learned_spells");
    public static final Type<RequestLearnedSpellsPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestLearnedSpellsPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RequestLearnedSpellsPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                PlayerLearnedSpells learnedSpells = player.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
                PacketDistributor.sendToPlayer(player, new SyncLearnedSpellsPacket(learnedSpells.serializeNBT(player.registryAccess())));
            }
        });
    }
}