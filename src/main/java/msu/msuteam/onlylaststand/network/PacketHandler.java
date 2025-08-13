package msu.msuteam.onlylaststand.network;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OnlyLastStand.MODID)
                .versioned("1.0");

        registrar.playToServer(OpenAccessoryScreenPacket.TYPE, OpenAccessoryScreenPacket.STREAM_CODEC,
                OpenAccessoryScreenPacket::handle);

        // --- ДОБАВЬТЕ ЭТУ СТРОКУ ---
        registrar.playToServer(OpenSpellScreenPacket.TYPE, OpenSpellScreenPacket.STREAM_CODEC,
                OpenSpellScreenPacket::handle);
        // -------------------------
    }
}