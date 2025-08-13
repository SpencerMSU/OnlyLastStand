package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.SpellMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SpellScreen extends AbstractContainerScreen<SpellMenu> {

    public SpellScreen(SpellMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 186; // Высота увеличена для 5 рядов
        this.titleLabelY = -1000;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        int topColor = 0xFF353535;
        int bottomColor = 0xFF4F4F4F;

        // Отрисовка основного фона
        pGuiGraphics.fillGradient(x, y, x + this.imageWidth, y + this.imageHeight, topColor, bottomColor);

        int borderColor = 0xFF000000; // Черный цвет для границ

        // Отрисовка границ для ячеек заклинаний
        int spellInvX = x + 62;
        int spellInvY = y + 8;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                int slotX = spellInvX + j * 18;
                int slotY = spellInvY + i * 18;
                pGuiGraphics.drawManaged(() -> {
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY - 1, borderColor);
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX - 1, slotY - 1, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX + 17, slotY - 1, slotY + 17, borderColor);
                });
            }
        }

        // Линия-разделитель
        int separatorY = y + 100;
        int separatorColor = 0xFF202020;
        pGuiGraphics.fill(x + 4, separatorY, x + this.imageWidth - 4, separatorY + 1, separatorColor);

        // --- ДОБАВЛЕНО: Отрисовка границ для основного инвентаря игрока ---
        int playerInvX = x + 8;
        int playerInvY = y + 104;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                int slotX = playerInvX + j * 18;
                int slotY = playerInvY + i * 18;
                pGuiGraphics.drawManaged(() -> {
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY - 1, borderColor);
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX - 1, slotY - 1, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX + 17, slotY - 1, slotY + 17, borderColor);
                });
            }
        }

        // --- ДОБАВЛЕНО: Отрисовка границ для хотбара ---
        int hotbarY = y + 162; // 104 + 58
        for (int i = 0; i < 9; ++i) {
            int slotX = playerInvX + i * 18;
            int slotY = hotbarY;
            pGuiGraphics.drawManaged(() -> {
                pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY - 1, borderColor);
                pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY + 17, borderColor);
                pGuiGraphics.vLine(slotX - 1, slotY - 1, slotY + 17, borderColor);
                pGuiGraphics.vLine(slotX + 17, slotY - 1, slotY + 17, borderColor);
            });
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}