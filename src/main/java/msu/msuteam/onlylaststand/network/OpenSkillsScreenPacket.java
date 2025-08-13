package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.SkillsMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenSkillsScreenPacket() implements CustomPacketPayload {
    public static final OpenSkillsScreenPacket INSTANCE = new OpenSkillsScreenPacket();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "open_skills_screen");
    public static final Type<OpenSkillsScreenPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenSkillsScreenPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OpenSkillsScreenPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                // ИСПРАВЛЕНО: Теперь лямбда вызывает правильный конструктор (id, inv),
                // который соответствует SimpleMenuProvider
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new SkillsMenu(id, inv),
                        Component.literal("Skills")
                ));
            }
        });
    }
}