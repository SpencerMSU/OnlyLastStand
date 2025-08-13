package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class ManaHud {

    public static int selectedSlot = 0;

    // ИСПРАВЛЕНО: Убран неиспользуемый параметр partialTicks
    public static void render(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;

        PlayerMana mana = minecraft.player.getData(ModAttachments.PLAYER_MANA);

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // --- Шкала Маны ---
        int manaBarWidth = 20;
        int manaBarHeight = 100;
        int x = screenWidth - manaBarWidth - 25;
        int y = screenHeight / 2 - manaBarHeight / 2 - 50;

        guiGraphics.fill(x - 2, y - 2, x + manaBarWidth + 2, y + manaBarHeight + 2, 0xFF000000);
        guiGraphics.fill(x, y, x + manaBarWidth, y + manaBarHeight, 0xFF333333);

        float manaPercentage = mana.getCurrentMana() / mana.getMaxMana();
        int filledHeight = (int) (manaBarHeight * manaPercentage);
        guiGraphics.fill(x, y + manaBarHeight - filledHeight, x + manaBarWidth, y + manaBarHeight, 0xFF00BFFF);

        // --- Слоты для заклинаний (2x5) ---
        int slotSize = 20;
        int slotSpacing = 4;

        int gridStartX = screenWidth - (slotSize * 2 + slotSpacing) - 10;
        int gridStartY = y + manaBarHeight + 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                int slotX = gridStartX + col * (slotSize + slotSpacing);
                int slotY = gridStartY + row * (slotSize + slotSpacing);

                int currentSlotIndex = row * 2 + col;
                boolean isSelected = (currentSlotIndex == selectedSlot);

                int frameColor = isSelected ? 0xFFFFFFFF : 0xFF000000;
                guiGraphics.fill(slotX - 2, slotY - 2, slotX + slotSize + 2, slotY + slotSize + 2, frameColor);

                guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF333333);

                if (isSelected) {
                    guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0x40FFFFFF);
                }
            }
        }
    }
}