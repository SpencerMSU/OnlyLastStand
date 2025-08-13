package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenSpellScreenPacket() implements CustomPacketPayload {

    public static final OpenSpellScreenPacket INSTANCE = new OpenSpellScreenPacket();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "open_spell_screen");
    public static final Type<OpenSpellScreenPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenSpellScreenPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OpenSpellScreenPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new SpellMenu(id, inv, serverPlayer.getData(ModAttachments.SPELL_INVENTORY)),
                        Component.translatable("container.onlylaststand.spells")
                ));
            }
        });
    }
}