package msu.msuteam.onlylaststand.client.gui;

import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.util.Mth;

public class SkillInfoWidget extends AbstractWidget {

    private final PlayerSkill skill;
    private final PlayerSkills.SkillData skillData;
    private final ItemStack iconStack;

    public SkillInfoWidget(int pX, int pY, int pWidth, int pHeight, PlayerSkill skill, PlayerSkills.SkillData skillData) {
        super(pX, pY, pWidth, pHeight, skill.getDisplayName());
        this.skill = skill;
        this.skillData = skillData;
        this.iconStack = getIconForItem(skill);
    }

    private ItemStack getIconForItem(PlayerSkill skill) {
        return switch (skill) {
            case COMBAT -> new ItemStack(Items.DIAMOND_SWORD);
            case FARMING -> new ItemStack(Items.DIAMOND_HOE);
            case MINING -> new ItemStack(Items.DIAMOND_PICKAXE);
            case SMITHING -> new ItemStack(Items.ANVIL);
            case CRAFTING -> new ItemStack(Items.CRAFTING_TABLE);
            case VITALITY -> new ItemStack(Items.GOLDEN_APPLE);
            case MAGIC -> new ItemStack(Items.ENCHANTING_TABLE);
            case ACCURACY -> new ItemStack(Items.BOW);
            case RADIATION_RESISTANCE -> new ItemStack(Items.MILK_BUCKET);
        };
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF000000);
        pGuiGraphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, 0xFF303030);

        Font font = Minecraft.getInstance().font;

        pGuiGraphics.renderFakeItem(this.iconStack, this.getX() + 12, this.getY() + 5);

        pGuiGraphics.drawString(font, this.skill.getDisplayName(), this.getX() + 40, this.getY() + 6, 0xFFFFFF);

        String levelText = String.format("%d / %d", this.skillData.getLevel(), this.skillData.getMaxLevel());
        pGuiGraphics.drawString(font, levelText, this.getX() + this.width - font.width(levelText) - 4, this.getY() + 6, 0xAAAAAA);

        // --- ИЗМЕНЕНИЕ ЗДЕСЬ ---
        // Используем трансформацию для масштабирования текста
        pGuiGraphics.pose().pushPose();
        float scale = 0.75f; // Уменьшаем текст на 25%
        pGuiGraphics.pose().scale(scale, scale, scale);
        // Пересчитываем координаты с учетом масштаба
        pGuiGraphics.drawString(font, this.skill.getDescription(), (int)((this.getX() + 40) / scale), (int)((this.getY() + 18) / scale), 0xAAAAAA);
        pGuiGraphics.pose().popPose();
        // -------------------------

        int xpBarY = this.getY() + this.height - 14;
        float progress = (float) this.skillData.getExperience() / this.skillData.getXpNeeded();
        int progressPixels = (int) ((this.width - 8) * Mth.clamp(progress, 0.0F, 1.0F));

        pGuiGraphics.fill(this.getX() + 4, xpBarY, this.getX() + this.width - 4, xpBarY + 8, 0xFF101010);
        pGuiGraphics.fill(this.getX() + 4, xpBarY, this.getX() + 4 + progressPixels, xpBarY + 8, 0xFF55FF55);

        String xpText = String.format("%d / %d XP", this.skillData.getExperience(), this.skillData.getXpNeeded());
        int textWidth = font.width(xpText);
        pGuiGraphics.drawString(font, xpText, this.getX() + (this.width / 2) - (textWidth / 2), xpBarY, 0xFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}