package msu.msuteam.onlylaststand.client;

import com.mojang.blaze3d.platform.InputConstants;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.core.ModMenuTypes;
import msu.msuteam.onlylaststand.network.OpenAccessoryScreenPacket;
import msu.msuteam.onlylaststand.network.OpenSkillsScreenPacket;
import msu.msuteam.onlylaststand.network.OpenSpellScreenPacket;
import msu.msuteam.onlylaststand.network.TryUpgradePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AnvilMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = OnlyLastStand.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        Keybindings.register(event);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ACCESSORY_MENU.get(), AccessoryScreen::new);
        event.register(ModMenuTypes.SPELL_MENU.get(), SpellScreen::new);
        event.register(ModMenuTypes.SKILLS_MENU.get(), SkillsScreen::new);
    }

    @EventBusSubscriber(modid = OnlyLastStand.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            if (Keybindings.OPEN_ACCESSORY_KEY.consumeClick()) {
                PacketDistributor.sendToServer(OpenAccessoryScreenPacket.INSTANCE);
            }
            if (Keybindings.OPEN_SPELL_KEY.consumeClick()) {
                PacketDistributor.sendToServer(OpenSpellScreenPacket.INSTANCE);
            }
        }

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            ManaHud.render(event.getGuiGraphics());
        }

        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
            Minecraft mc = Minecraft.getInstance();
            long windowHandle = mc.getWindow().getWindow();
            if (mc.player != null && (InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_ALT))) {
                double scrollDelta = event.getScrollDeltaY();
                if (scrollDelta > 0) {
                    ManaHud.selectedSlot = (ManaHud.selectedSlot - 1 + 10) % 10;
                } else if (scrollDelta < 0) {
                    ManaHud.selectedSlot = (ManaHud.selectedSlot + 1) % 10;
                }
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onScreenMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
            if (event.getScreen() instanceof AnvilScreen) {
                AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) event.getScreen();
                if (screen.getSlotUnderMouse() != null && screen.getSlotUnderMouse().index == AnvilMenu.RESULT_SLOT) {
                    if (screen.getMenu().getSlot(AnvilMenu.RESULT_SLOT).hasItem()) {
                        PacketDistributor.sendToServer(TryUpgradePacket.INSTANCE);
                        event.setCanceled(true);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onScreenInit(ScreenEvent.Init.Post event) {
            if (event.getScreen() instanceof InventoryScreen screen) {
                event.addListener(Button.builder(Component.literal("Skills"), (button) -> {
                    PacketDistributor.sendToServer(OpenSkillsScreenPacket.INSTANCE);
                }).bounds(screen.getGuiLeft() + 128, screen.getGuiTop() + 60, 40, 20).build());
            }
        }
    }
}