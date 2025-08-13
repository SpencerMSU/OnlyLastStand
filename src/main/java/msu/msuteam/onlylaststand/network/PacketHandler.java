package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import msu.msuteam.onlylaststand.network.OpenSkillsScreenPacket;
public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OnlyLastStand.MODID)
                .versioned("1.0");

        registrar.playToServer(OpenAccessoryScreenPacket.TYPE, OpenAccessoryScreenPacket.STREAM_CODEC,
                OpenAccessoryScreenPacket::handle);

        registrar.playToServer(OpenSpellScreenPacket.TYPE, OpenSpellScreenPacket.STREAM_CODEC,
                OpenSpellScreenPacket::handle);
        registrar.playToServer(TryUpgradePacket.TYPE, TryUpgradePacket.STREAM_CODEC, TryUpgradePacket::handle);
        registrar.playToServer(OpenSkillsScreenPacket.TYPE, OpenSkillsScreenPacket.STREAM_CODEC, OpenSkillsScreenPacket::handle);
        registrar.playToServer(RequestSkillsDataPacket.TYPE, RequestSkillsDataPacket.STREAM_CODEC, RequestSkillsDataPacket::handle);
        registrar.playToClient(SyncSkillsDataPacket.TYPE, SyncSkillsDataPacket.STREAM_CODEC, SyncSkillsDataPacket::handle);
    }
}