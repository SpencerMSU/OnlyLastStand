package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.event.PlayerEventHandler;
import msu.msuteam.onlylaststand.inventory.AccessoryMenu;
import msu.msuteam.onlylaststand.util.CollectionType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AccessoryScreen extends AbstractContainerScreen<AccessoryMenu> {

    private final int bonusPanelWidth = 122;

    public AccessoryScreen(AccessoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.titleLabelY = -1000; // Прячем стандартный заголовок
    }

    @Override
    protected void init() {
        super.init();
        // ИСПРАВЛЕНИЕ: Переопределяем leftPos, чтобы центрировать всю конструкцию, включая боковую панель.
        // Это не даст элементам "съехать".
        int totalWidth = this.imageWidth + this.bonusPanelWidth;
        this.leftPos = (this.width - totalWidth) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        int topColor = 0xFF353535;
        int bottomColor = 0xFF4F4F4F;

        // Отрисовка основной панели
        pGuiGraphics.fillGradient(x, y, x + this.imageWidth, y + this.imageHeight, topColor, bottomColor);

        // Отрисовка панели бонусов справа
        int bonusPanelX = x + this.imageWidth;
        pGuiGraphics.fillGradient(bonusPanelX, y, bonusPanelX + this.bonusPanelWidth, y + this.imageHeight, topColor, bottomColor);

        // Отрисовка "углубления" под слоты аксессуаров для их выделения
        int slotGridX = x + 61 - 4;
        int slotGridY = y + 18 - 4;
        int slotGridWidth = 18 * 3 + 8;
        int slotGridHeight = 18 * 3 + 8;
        pGuiGraphics.fill(slotGridX, slotGridY, slotGridX + slotGridWidth, slotGridY + slotGridHeight, 0xFF202020);

        // ДОБАВЛЕНО: Линия-разделитель между аксессуарами и инвентарем
        int separatorY = y + 78;
        int separatorColor = 0xFF202020;
        pGuiGraphics.fill(x + 4, separatorY, x + this.imageWidth - 4, separatorY + 1, separatorColor);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Сначала вызываем super.render(), который отрисует слоты в правильном, вычисленном нами месте
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        renderSetBonusInfo(pGuiGraphics);
    }

    private void renderSetBonusInfo(GuiGraphics graphics) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        // Используем this.leftPos и this.topPos, которые теперь всегда корректны
        int x = this.leftPos;
        int y = this.topPos;

        int textX = x + this.imageWidth + 8;
        int textY = y + 8;
        int maxWidth = this.bonusPanelWidth - 16;

        List<Component> lines = new ArrayList<>();

        if (PlayerEventHandler.isWearingFullSet(this.minecraft.player, CollectionType.FIRE)) {
            lines.add(Component.translatable("collection.onlylaststand.fire").withStyle(ChatFormatting.BOLD));
            lines.add(Component.translatable("setbonus.onlylaststand.fire_new1"));
            lines.add(Component.translatable("setbonus.onlylaststand.fire_new2"));
            lines.add(Component.translatable("setbonus.onlylaststand.fire_new3"));
            lines.add(Component.translatable("setbonus.onlylaststand.fire_debuff1"));

        } else if (PlayerEventHandler.isWearingFullSet(this.minecraft.player, CollectionType.WATER)) {
            lines.add(Component.translatable("collection.onlylaststand.water").withStyle(ChatFormatting.BOLD));
            lines.add(Component.translatable("setbonus.onlylaststand.water1"));
            lines.add(Component.translatable("setbonus.onlylaststand.water2"));
            lines.add(Component.translatable("setbonus.onlylaststand.water3"));
        }

        int currentY = textY;
        for (Component line : lines) {
            graphics.drawWordWrap(this.font, line, textX, currentY, maxWidth, 0xAAAAAA);
            int height = this.font.wordWrapHeight(line, maxWidth);
            currentY += height;

            if (lines.indexOf(line) == 0 && height > 0) {
                currentY += 2;
            }
        }
    }
}