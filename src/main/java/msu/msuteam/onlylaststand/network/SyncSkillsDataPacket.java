package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.client.SkillsScreen;
import msu.msuteam.onlylaststand.client.SpellScreen;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
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
            var mc = Minecraft.getInstance();
            if (mc.player != null) {
                // 1) Обновляем клиентский аттачмент навыков: теперь SpellScreen видит актуальное число слотов
                mc.player.getData(ModAttachments.PLAYER_SKILLS)
                        .deserializeNBT(mc.player.registryAccess(), packet.skillsNbt);
            }

            // Обновляем экран навыков, если он открыт
            if (mc.screen instanceof SkillsScreen skillsScreen) {
                skillsScreen.updateSkillsData(packet.skillsNbt);
            }
            // 2) Форсим перерисовку экрана заклинаний — пропадет “бедрок” на открытых слотах
            if (mc.screen instanceof SpellScreen spellScreen) {
                spellScreen.updateSpellDisplay();
            }
        });
    }
}