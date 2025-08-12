package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.AccessoryMenu;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.event.PlayerEventHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AccessoryScreen extends AbstractContainerScreen<AccessoryMenu> {

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

        int topColor = 0xFF202020;
        int bottomColor = 0xFF404040;

        pGuiGraphics.fillGradient(x, y, x + this.imageWidth, y + this.imageHeight, topColor, bottomColor);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        renderSetBonusInfo(pGuiGraphics);
    }

    private void renderSetBonusInfo(GuiGraphics graphics) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        int x = this.leftPos + this.imageWidth + 5;
        int y = this.topPos;

        if (PlayerEventHandler.isWearingFullSet(this.minecraft.player, CollectionType.FIRE)) {
            graphics.drawString(this.font, Component.translatable("collection.onlylaststand.fire"), x, y, 0xFF5555, false);
            graphics.drawString(this.font, Component.translatable("setbonus.onlylaststand.fire_new1"), x, y + 12, 0xAAAAAA, false);
            graphics.drawString(this.font, Component.translatable("setbonus.onlylaststand.fire_new2"), x, y + 22, 0xAAAAAA, false);
        } else if (PlayerEventHandler.isWearingFullSet(this.minecraft.player, CollectionType.WATER)) {
            graphics.drawString(this.font, Component.translatable("collection.onlylaststand.water"), x, y, 0x5555FF, false);
            graphics.drawString(this.font, Component.translatable("setbonus.onlylaststand.water1"), x, y + 12, 0xAAAAAA, false);
            graphics.drawString(this.font, Component.translatable("setbonus.onlylaststand.water2"), x, y + 22, 0xAAAAAA, false);
            graphics.drawString(this.font, Component.translatable("setbonus.onlylaststand.water3"), x, y + 32, 0xAAAAAA, false);
        }
    }
}