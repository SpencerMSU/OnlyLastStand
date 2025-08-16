package msu.msuteam.onlylaststand.client;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ManaHud {

    public static int selectedSlot = 0;
    private static final ItemStack LOCKED_SLOT_ICON = new ItemStack(Items.BEDROCK);

    public static void render(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;

        PlayerMana mana = minecraft.player.getData(ModAttachments.PLAYER_MANA);
        PlayerSkills skills = minecraft.player.getData(ModAttachments.PLAYER_SKILLS);
        int unlockedSlots = skills.getUnlockedSpellSlots();

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int manaBarWidth = 20;
        int manaBarHeight = 100;
        int x = screenWidth - manaBarWidth - 25;
        int y = screenHeight / 2 - manaBarHeight / 2 - 50;

        guiGraphics.fill(x - 2, y - 2, x + manaBarWidth + 2, y + manaBarHeight + 2, 0xFF000000);
        guiGraphics.fill(x, y, x + manaBarWidth, y + manaBarHeight, 0xFF333333);
        float manaPercentage = mana.getCurrentMana() / mana.getMaxMana();
        int filledHeight = (int) (manaBarHeight * manaPercentage);
        guiGraphics.fill(x, y + manaBarHeight - filledHeight, x + manaBarWidth, y + manaBarHeight, 0xFF00BFFF);

        Font font = minecraft.font;
        String manaText = String.format("%d/%d", (int)mana.getCurrentMana(), (int)mana.getMaxMana());
        int textWidth = font.width(manaText);
        guiGraphics.pose().pushPose();
        float scale = 0.8f;
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(font, manaText, (int)((x + manaBarWidth / 2 - textWidth * scale / 2) / scale), (int)((y - 10) / scale), 0xFFFFFF);
        guiGraphics.pose().popPose();

        int slotSize = 20;
        int slotSpacing = 4;

        int gridStartX = screenWidth - (slotSize * 2 + slotSpacing) - 10;
        int gridStartY = y + manaBarHeight + 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                int slotX = gridStartX + col * (slotSize + slotSpacing);
                int slotY = gridStartY + row * (slotSize + slotSpacing);

                int currentSlotIndex = row * 2 + col;

                if (currentSlotIndex < unlockedSlots) {
                    boolean isSelected = (currentSlotIndex == selectedSlot);
                    int frameColor = isSelected ? 0xFFFFFFFF : 0xFF000000;
                    guiGraphics.fill(slotX - 2, slotY - 2, slotX + slotSize + 2, slotY + slotSize + 2, frameColor);
                    guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF333333);
                    if (isSelected) {
                        guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0x40FFFFFF);
                    }
                    ItemStack spellStack = minecraft.player.getData(ModAttachments.SPELL_INVENTORY).getStackInSlot(currentSlotIndex);
                    if (!spellStack.isEmpty()) {
                        guiGraphics.renderFakeItem(spellStack, slotX + 2, slotY + 2);
                    }
                } else {
                    guiGraphics.fill(slotX - 2, slotY - 2, slotX + slotSize + 2, slotY + slotSize + 2, 0xFF000000);
                    guiGraphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF101010);
                    guiGraphics.renderFakeItem(LOCKED_SLOT_ICON, slotX + 2, slotY + 2);
                }
            }
        }
    }
}