package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OnlyLastStand.MODID)
                .versioned("1.0");

        registrar.playToServer(OpenAccessoryScreenPacket.TYPE, OpenAccessoryScreenPacket.STREAM_CODEC, OpenAccessoryScreenPacket::handle);
        registrar.playToServer(OpenSpellScreenPacket.TYPE, OpenSpellScreenPacket.STREAM_CODEC, OpenSpellScreenPacket::handle);
        registrar.playToServer(TryUpgradePacket.TYPE, TryUpgradePacket.STREAM_CODEC, TryUpgradePacket::handle);
        registrar.playToServer(OpenSkillsScreenPacket.TYPE, OpenSkillsScreenPacket.STREAM_CODEC, OpenSkillsScreenPacket::handle);
        registrar.playToServer(RequestSkillsDataPacket.TYPE, RequestSkillsDataPacket.STREAM_CODEC, RequestSkillsDataPacket::handle);
        registrar.playToClient(SyncSkillsDataPacket.TYPE, SyncSkillsDataPacket.STREAM_CODEC, SyncSkillsDataPacket::handle);
        registrar.playToServer(CastSpellPacket.TYPE, CastSpellPacket.STREAM_CODEC, CastSpellPacket::handle);
        registrar.playToClient(SyncManaPacket.TYPE, SyncManaPacket.STREAM_CODEC, SyncManaPacket::handle);
        registrar.playToClient(DisplayNotificationPacket.TYPE, DisplayNotificationPacket.STREAM_CODEC, DisplayNotificationPacket::handle);
        registrar.playToClient(SyncSpellsPacket.TYPE, SyncSpellsPacket.STREAM_CODEC, SyncSpellsPacket::handle);
        registrar.playToClient(SpawnParticlesPacket.TYPE, SpawnParticlesPacket.STREAM_CODEC, SpawnParticlesPacket::handle);
        registrar.playToClient(SyncLearnedSpellsPacket.TYPE, SyncLearnedSpellsPacket.STREAM_CODEC, SyncLearnedSpellsPacket::handle);
        registrar.playToServer(RequestLearnedSpellsPacket.TYPE, RequestLearnedSpellsPacket.STREAM_CODEC, RequestLearnedSpellsPacket::handle);

        // ДОБАВЛЕНО
        registrar.playToServer(ModifySpellSlotPacket.TYPE, ModifySpellSlotPacket.STREAM_CODEC, ModifySpellSlotPacket::handle);
    }
}