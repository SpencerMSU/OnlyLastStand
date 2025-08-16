package msu.msuteam.onlylaststand.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font; // <-- ДОБАВЛЕН ИМПОРТ
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static final List<Notification> NOTIFICATIONS = new ArrayList<>();

    public static void show(Component message) {
        NOTIFICATIONS.add(new Notification(message));
    }

    public static void tick() {
        NOTIFICATIONS.removeIf(Notification::isExpired);
        for (Notification notification : NOTIFICATIONS) {
            notification.tick();
        }
    }

    public static void render(GuiGraphics guiGraphics) {
        int yOffset = 0;
        for (Notification notification : NOTIFICATIONS) {
            notification.render(guiGraphics, yOffset);
            yOffset += 12;
        }
    }

    private static class Notification {
        private final Component message;
        private int ticksRemaining;
        private static final int MAX_TICKS = 60; // 3 секунды

        Notification(Component message) {
            this.message = message;
            this.ticksRemaining = MAX_TICKS;
        }

        void tick() {
            this.ticksRemaining--;
        }

        boolean isExpired() {
            return this.ticksRemaining <= 0;
        }

        void render(GuiGraphics guiGraphics, int yOffset) {
            Minecraft mc = Minecraft.getInstance();
            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();
            Font font = mc.font;

            float alpha = 1.0f;
            if (this.ticksRemaining < 20) { // Fade out
                alpha = this.ticksRemaining / 20.0f;
            }

            int color = (int) (alpha * 255.0f) << 24 | 0xFFFFFF;
            float yPos = screenHeight / 2.0f - 30 - yOffset + (MAX_TICKS - this.ticksRemaining) * 0.5f;

            guiGraphics.drawCenteredString(font, this.message, screenWidth / 2, (int) yPos, color);
        }
    }
}