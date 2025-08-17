package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.client.SpellScreen;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncLearnedSpellsPacket(CompoundTag learnedSpellsTag) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "sync_learned_spells");
    public static final Type<SyncLearnedSpellsPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLearnedSpellsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncLearnedSpellsPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncLearnedSpellsPacket(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncLearnedSpellsPacket packet) {
            buf.writeNbt(packet.learnedSpellsTag);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncLearnedSpellsPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                PlayerLearnedSpells learnedSpells = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
                learnedSpells.deserializeNBT(Minecraft.getInstance().player.registryAccess(), packet.learnedSpellsTag);

                // ИСПРАВЛЕНО: Если экран заклинаний открыт, принудительно обновляем его содержимое
                if (Minecraft.getInstance().screen instanceof SpellScreen screen) {
                    screen.updateSpellDisplay();
                }
            }
        });
    }
}