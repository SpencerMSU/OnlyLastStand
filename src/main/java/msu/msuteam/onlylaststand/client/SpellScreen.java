package msu.msuteam.onlylaststand.client;

import com.mojang.blaze3d.systems.RenderSystem;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellMenu;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class SpellScreen extends AbstractContainerScreen<SpellMenu> {

    private static final ResourceLocation TABS_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tabs.png");
    private static final ResourceLocation BG_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_items.png");

    public SpellScreen(SpellMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 195;
        this.imageHeight = 220; // Новая высота
        this.titleLabelY = -1000;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        // Заполняем "каталог" изученными заклинаниями
        populateLearnedSpells();
    }

    private void populateLearnedSpells() {
        PlayerLearnedSpells learnedSpells = this.minecraft.player.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
        List<Holder<Item>> allFireSpells = BuiltInRegistries.ITEM.getTag(ModTags.Items.FIRE_SPELLS)
                .map(tag -> tag.stream().toList())
                .orElse(List.of());

        int currentSlot = 0;
        for (Holder<Item> spellHolder : allFireSpells) {
            if (learnedSpells.hasLearned(spellHolder.value())) {
                if (currentSlot < this.menu.learnedSpellsContainer.getContainerSize()) {
                    this.menu.learnedSpellsContainer.setItem(currentSlot, new ItemStack(spellHolder.value()));
                    currentSlot++;
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Рисуем фон, похожий на креативный
        pGuiGraphics.blit(BG_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Рисуем активную вкладку "Огонь"
        pGuiGraphics.blit(TABS_TEXTURE, x - 28, y + 4, 28, 0, 28, 32);
        pGuiGraphics.renderFakeItem(new ItemStack(Items.BLAZE_POWDER), x - 20, y + 10);

        // Рисуем сетку для изученных заклинаний
        int learnedGridX = x + 5;
        int learnedGridY = y + 15;
        for (int i = 0; i < 45; i++) { // 5 рядов по 9 слотов
            int slotX = learnedGridX + (i % 9) * 18;
            int slotY = learnedGridY + (i / 9) * 18;
            pGuiGraphics.blit(BG_TEXTURE, slotX, slotY, 7, 7, 18, 18);
        }

        // Рисуем фон для 10 активных слотов (внизу)
        int activeSlotsBgX = x + 17;
        int activeSlotsBgY = y + 110;
        pGuiGraphics.fill(activeSlotsBgX, activeSlotsBgY, activeSlotsBgX + 142, activeSlotsBgY + 50, 0x80000000);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Не рисуем стандартные надписи
    }
}