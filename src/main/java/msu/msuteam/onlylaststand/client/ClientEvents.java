package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.core.ModMenuTypes;
import msu.msuteam.onlylaststand.network.OpenAccessoryScreenPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber(modid = OnlyLastStand.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        Keybindings.register(event);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ACCESSORY_MENU.get(), AccessoryScreen::new);
    }

    @EventBusSubscriber(modid = OnlyLastStand.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            if (Keybindings.OPEN_ACCESSORY_KEY.consumeClick()) {
                PacketDistributor.sendToServer(OpenAccessoryScreenPacket.INSTANCE);
            }
        }
    }
}