package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellMenu;
import msu.msuteam.onlylaststand.network.PickupVirtualSpellPacket;
import msu.msuteam.onlylaststand.network.RequestLearnedSpellsPacket;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class SpellScreen extends AbstractContainerScreen<SpellMenu> {

    private static final ItemStack LOCKED_SLOT_ICON = new ItemStack(Items.BEDROCK);
    private int selectedTab = 0;
    private final List<ItemStack> catalogItems = new ArrayList<>();

    public SpellScreen(SpellMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 98;
        this.imageHeight = 112;
        this.titleLabelY = -1000;
        this.inventoryLabelY = -1000;
    }

    @Override
    protected void init() {
        super.init();
        PacketDistributor.sendToServer(RequestLearnedSpellsPacket.INSTANCE);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // ВАЖНО: Вызываем super.render() ПЕРЕД всем остальным.
        // Это установит hoveredSlot и отрисует фон и слоты.
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // Теперь отрисовываем подсказки поверх всего, включая каталог.
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        // И в самом конце рисуем предмет на курсоре.
        pGuiGraphics.renderItem(this.menu.getCarried(), pMouseX - 8, pMouseY - 8);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        // Сначала вызываем стандартный метод для подсказок над слотами.
        super.renderTooltip(pGuiGraphics, pX, pY);

        // Затем добавляем нашу логику для подсказок над каталогом.
        for (int i = 0; i < this.catalogItems.size(); i++) {
            int col = i % 2;
            int row = i / 2;
            int slotX = this.leftPos + 8 + col * 18;
            int slotY = this.topPos + 18 + row * 18;
            if (isMouseOver(pX, pY, slotX, slotY, 16, 16)) {
                pGuiGraphics.renderTooltip(this.font, this.catalogItems.get(i), pX, pY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Всю нашу кастомную отрисовку переносим сюда.
        renderGuiElements(pGuiGraphics);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Оставляем пустым, чтобы не было стандартных надписей "Inventory" и т.д.
    }

    private void renderGuiElements(GuiGraphics pGuiGraphics) {
        int x = this.leftPos;
        int y = this.topPos;

        pGuiGraphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xDD222222);

        int fireTabColor = (selectedTab == 0) ? 0xFF3F3F3F : 0xFF2E2E2E;
        pGuiGraphics.fill(x - 28, y + 4, x, y + 32, fireTabColor);
        pGuiGraphics.renderFakeItem(new ItemStack(Items.BLAZE_POWDER), x - 22, y + 10);

        int waterTabColor = (selectedTab == 1) ? 0xFF3F3F3F : 0xFF2E2E2E;
        pGuiGraphics.fill(x - 28, y + 36, x, y + 64, waterTabColor);
        pGuiGraphics.renderFakeItem(new ItemStack(Items.WATER_BUCKET), x - 22, y + 42);

        int borderColor = 0xFF000000;
        int slotColor = 0xFF1F1F1F;

        for (int i = 0; i < 10; i++) {
            int col = i % 2;
            int row = i / 2;
            int slotX = x + 7 + col * 18;
            int slotY = y + 17 + row * 18;
            pGuiGraphics.fill(slotX, slotY, slotX + 18, slotY + 18, slotColor);
            renderSlotBorder(pGuiGraphics, slotX + 1, slotY + 1, borderColor);
            if (i < this.catalogItems.size()) {
                pGuiGraphics.renderFakeItem(this.catalogItems.get(i), slotX + 1, slotY + 1);
            }
        }

        pGuiGraphics.vLine(x + 47, y + 16, y + this.imageHeight - 1, borderColor);

        PlayerSkills skills = this.minecraft.player.getData(ModAttachments.PLAYER_SKILLS);
        int unlockedSlots = skills.getUnlockedSpellSlots();
        for (Slot slot : this.menu.slots) {
            int slotX = x + slot.x;
            int slotY = y + slot.y;
            // Убираем отрисовку слотов отсюда, так как она теперь в super.render()
            if (slot.index >= unlockedSlots) {
                pGuiGraphics.renderFakeItem(LOCKED_SLOT_ICON, slotX, slotY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        // --- ИСПРАВЛЕНИЕ #1: Возвращаем логику для предотвращения выбрасывания ---
        boolean isOutside = pMouseX < this.leftPos || pMouseY < this.topPos || pMouseX >= this.leftPos + this.imageWidth || pMouseY >= this.topPos + this.imageHeight;
        if (pButton == 0 && !this.menu.getCarried().isEmpty() && isOutside) {
            // Просто очищаем курсор на клиенте. Сервер сделает то же самое при следующем обновлении.
            this.menu.setCarried(ItemStack.EMPTY);
            return true; // "Съедаем" клик
        }
        // --------------------------------------------------------------------------

        if (pButton == 0) {
            for (int i = 0; i < this.catalogItems.size(); i++) {
                int col = i % 2;
                int row = i / 2;
                int slotX = this.leftPos + 8 + col * 18;
                int slotY = this.topPos + 18 + row * 18;
                if (isMouseOver(pMouseX, pMouseY, slotX, slotY, 16, 16)) {
                    ItemStack clickedStack = this.catalogItems.get(i);
                    if (!clickedStack.is(Items.BARRIER) && this.menu.getCarried().isEmpty()) {
                        PacketDistributor.sendToServer(new PickupVirtualSpellPacket(BuiltInRegistries.ITEM.getKey(clickedStack.getItem())));
                        return true;
                    }
                }
            }
            if (handleTabClick(pMouseX, pMouseY)) return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private boolean handleTabClick(double pMouseX, double pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        if (isMouseOver(pMouseX, pMouseY, x - 28, y + 4, 28, 28) && selectedTab != 0) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.selectedTab = 0;
            updateAndRequestSpells();
            return true;
        }
        if (isMouseOver(pMouseX, pMouseY, x - 28, y + 36, 28, 28) && selectedTab != 1) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.selectedTab = 1;
            updateAndRequestSpells();
            return true;
        }
        return false;
    }

    private void updateAndRequestSpells() {
        PacketDistributor.sendToServer(RequestLearnedSpellsPacket.INSTANCE);
    }

    private boolean isMouseOver(double pMouseX, double pMouseY, int pX, int pY, int pWidth, int pHeight) {
        return pMouseX >= pX && pMouseY >= pY && pMouseX < pX + pWidth && pMouseY < pY + pHeight;
    }

    public void updateSpellDisplay() {
        PlayerLearnedSpells learnedSpells = this.minecraft.player.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
        this.catalogItems.clear();
        TagKey<Item> tagKey = (selectedTab == 0) ? ModTags.Items.FIRE_SPELLS : ModTags.Items.WATER_SPELLS;
        List<Holder<Item>> allSpellsInTab = BuiltInRegistries.ITEM.getTag(tagKey)
                .map(tag -> tag.stream().toList()).orElse(List.of());
        for (Holder<Item> spellHolder : allSpellsInTab) {
            Item spellItem = spellHolder.value();
            if (learnedSpells.hasLearned(spellItem)) {
                this.catalogItems.add(new ItemStack(spellItem));
            } else {
                ItemStack barrier = new ItemStack(Items.BARRIER);
                barrier.set(DataComponents.CUSTOM_NAME, spellItem.getDescription());
                this.catalogItems.add(barrier);
            }
        }
    }

    private void renderSlotBorder(GuiGraphics graphics, int x, int y, int color) {
        graphics.hLine(x - 1, x + 16, y - 1, color);
        graphics.hLine(x - 1, x + 16, y + 16, color);
        graphics.vLine(x - 1, y - 1, y + 17, color);
        graphics.vLine(x + 16, y - 1, y + 17, color);
    }
}