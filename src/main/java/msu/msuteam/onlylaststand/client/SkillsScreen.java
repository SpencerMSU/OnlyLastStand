package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.client.gui.SkillInfoWidget;
import msu.msuteam.onlylaststand.inventory.SkillsMenu;
import msu.msuteam.onlylaststand.network.RequestSkillsDataPacket;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class SkillsScreen extends AbstractContainerScreen<SkillsMenu> {

    private PlayerSkills skillsData;

    public SkillsScreen(SkillsMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 340; // Ширина для 3-х колонок
        this.imageHeight = 220; // Высота для 3-х рядов
        this.skillsData = new PlayerSkills(); // Создаем пустой объект для начала
    }

    @Override
    protected void init() {
        super.init();
        // При открытии экрана запрашиваем актуальные данные с сервера
        PacketDistributor.sendToServer(RequestSkillsDataPacket.INSTANCE);
    }

    // Этот метод будет вызван пакетом, когда придут данные с сервера
    public void updateSkillsData(CompoundTag nbt) {
        this.skillsData.deserializeNBT(this.minecraft.player.registryAccess(), nbt);
        rebuildWidgets();
    }

    public void rebuildWidgets() {
        this.clearWidgets();
        int widgetWidth = 100;
        int widgetHeight = 60;
        int xOffset = 10;
        int yOffset = 10;

        int col = 0;
        int row = 0;
        for (PlayerSkill skill : PlayerSkill.values()) {
            int x = this.leftPos + xOffset + (col * (widgetWidth + xOffset));
            int y = this.topPos + yOffset + (row * (widgetHeight + yOffset));

            this.addRenderableWidget(new SkillInfoWidget(x, y, widgetWidth, widgetHeight, skill, this.skillsData.getSkill(skill)));

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        pGuiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF202020);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}