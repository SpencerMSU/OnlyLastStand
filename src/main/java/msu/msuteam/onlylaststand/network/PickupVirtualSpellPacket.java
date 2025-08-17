package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.SpellMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record PickupVirtualSpellPacket(ResourceLocation spellId) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "pickup_virtual_spell");
    public static final Type<PickupVirtualSpellPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PickupVirtualSpellPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PickupVirtualSpellPacket decode(RegistryFriendlyByteBuf buf) {
            return new PickupVirtualSpellPacket(buf.readResourceLocation());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PickupVirtualSpellPacket packet) {
            buf.writeResourceLocation(packet.spellId);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PickupVirtualSpellPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.containerMenu instanceof SpellMenu spellMenu) {
                Optional<Item> spellItem = BuiltInRegistries.ITEM.getOptional(packet.spellId);
                spellItem.ifPresent(item -> {
                    // Эта команда обновит предмет на курсоре на сервере и отправит обновление клиенту
                    spellMenu.setCarried(new ItemStack(item));
                });
            }
        });
    }
}