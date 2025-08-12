package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.AccessoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AccessoryScreen extends AbstractContainerScreen<AccessoryMenu> {

    // Эта переменная больше не нужна, так как мы не используем текстуру
    // private static final ResourceLocation TEXTURE = ...;

    public AccessoryScreen(AccessoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.titleLabelY = -1000;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // ИСПРАВЛЕНО: Вместо текстуры рисуем красивый градиент
        // Вы можете поменять цвета, как вам нравится. Формат ARGB (Альфа, Красный, Зеленый, Синий)
        int topColor = 0xFF202020;    // Темно-серый, почти черный
        int bottomColor = 0xFF404040; // Чуть более светлый серый

        pGuiGraphics.fillGradient(x, y, x + this.imageWidth, y + this.imageHeight, topColor, bottomColor);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // ИСПРАВЛЕНО: Убрана боковая панель, осталась только стандартная подсказка
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        // renderSideInfoPanel(pGuiGraphics, pMouseX, pMouseY); // <- Эта строка удалена
    }

    // Метод renderSideInfoPanel(...) полностью удален, так как он больше не нужен
}