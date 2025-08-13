package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.event.PlayerEventHandler;
import msu.msuteam.onlylaststand.inventory.AccessoryMenu;
import msu.msuteam.onlylaststand.util.CollectionType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AccessoryScreen extends AbstractContainerScreen<AccessoryMenu> {

    private final int bonusPanelWidth = 122;

    public AccessoryScreen(AccessoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.titleLabelY = -1000;
    }

    @Override
    protected void init() {
        super.init();
        int totalWidth = this.imageWidth + this.bonusPanelWidth;
        this.leftPos = (this.width - totalWidth) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        int topColor = 0xFF353535;
        int bottomColor = 0xFF4F4F4F;

        pGuiGraphics.fillGradient(x, y, x + this.imageWidth, y + this.imageHeight, topColor, bottomColor);

        int bonusPanelX = x + this.imageWidth;
        pGuiGraphics.fillGradient(bonusPanelX, y, bonusPanelX + this.bonusPanelWidth, y + this.imageHeight, topColor, bottomColor);

        int borderColor = 0xFF000000;

        int accessorySlotsX = x + 61;
        int accessorySlotsY = y + 18;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int slotX = accessorySlotsX + j * 18;
                int slotY = accessorySlotsY + i * 18;
                pGuiGraphics.drawManaged(() -> {
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY - 1, borderColor);
                    pGuiGraphics.hLine(slotX - 1, slotX + 17, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX - 1, slotY - 1, slotY + 17, borderColor);
                    pGuiGraphics.vLine(slotX + 17, slotY - 1, slotY + 17, borderColor);
                });
            }
        }

        int separatorY = y + 78;
        int separatorColor = 0xFF202020;
        pGuiGraphics.fill(x + 4, separatorY, x + this.imageWidth - 4, separatorY + 1, separatorColor);

        int playerInvX = x + 8;
        int playerInvY = y + 86;
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

        int hotbarY = y + 144;
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
        renderSetBonusInfo(pGuiGraphics);
    }

    private void renderSetBonusInfo(GuiGraphics graphics) {
        if (this.minecraft == null || this.minecraft.player == null) return;

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