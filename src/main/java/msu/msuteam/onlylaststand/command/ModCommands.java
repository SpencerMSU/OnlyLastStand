package msu.msuteam.onlylaststand.command;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        MagicCommand.register(event.getDispatcher());
    }
}