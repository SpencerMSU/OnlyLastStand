package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellMenu extends AbstractContainerMenu {
    public static final int SLOTS = 10;

    public SpellMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.getData(ModAttachments.SPELL_INVENTORY));
    }

    public SpellMenu(int pContainerId, Inventory playerInventory, SpellInventory spellInventory) {
        super(ModMenuTypes.SPELL_MENU.get(), pContainerId);

        // Слоты для заклинаний 2x5
        int spellInvX = 62;
        int spellInvY = 8;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                // ИСПРАВЛЕНО: Используем наш новый SpellSlot
                this.addSlot(new SpellSlot(spellInventory, j + i * 2, spellInvX + j * 18, spellInvY + i * 18));
            }
        }

        // Инвентарь игрока
        int playerInvX = 8;
        int playerInvY = 104;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }

        // Хотбар
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, playerInvX + i * 18, playerInvY + 58));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (index < SLOTS) {
            if (!this.moveItemStackTo(sourceStack, SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Перед перемещением в слоты заклинаний, проверяем, можно ли туда положить предмет
            if (this.slots.get(0).mayPlace(sourceStack)) {
                if (!this.moveItemStackTo(sourceStack, 0, SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        return copyStack;
    }
}