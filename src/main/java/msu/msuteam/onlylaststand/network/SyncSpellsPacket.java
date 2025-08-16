package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncSpellsPacket(CompoundTag inventoryTag) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "sync_spells");
    public static final Type<SyncSpellsPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSpellsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncSpellsPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncSpellsPacket(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncSpellsPacket packet) {
            buf.writeNbt(packet.inventoryTag);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncSpellsPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                SpellInventory spellInventory = Minecraft.getInstance().player.getData(ModAttachments.SPELL_INVENTORY);
                spellInventory.deserializeNBT(Minecraft.getInstance().player.registryAccess(), packet.inventoryTag);
            }
        });
    }
}