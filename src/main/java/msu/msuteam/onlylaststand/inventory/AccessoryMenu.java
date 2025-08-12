package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AccessoryMenu extends AbstractContainerMenu {
    public static final int SLOTS = 9;
    private final AccessoryInventory accessoryInventory;

    // Этот конструктор вызывается, когда меню открывается с клиента
    public AccessoryMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.getData(ModAttachments.ACCESSORY_INVENTORY));
    }

    // Этот конструктор является основным и вызывается на сервере
    public AccessoryMenu(int pContainerId, Inventory playerInventory, AccessoryInventory accessoryInventory) {
        super(ModMenuTypes.ACCESSORY_MENU.get(), pContainerId);
        this.accessoryInventory = accessoryInventory;

        // Координаты слотов на экране GUI
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int START_X = 8;
        final int START_Y = 18;

        // Слоты для аксессуаров (3x3)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Мы используем SlotItemHandler, так как наш инвентарь - это ItemStackHandler
                addSlot(new SlotItemHandler(accessoryInventory, j + i * 3, START_X + 24 + j * SLOT_X_SPACING, START_Y + i * SLOT_Y_SPACING));
            }
        }

        // Инвентарь игрока (основная часть)
        int playerInvX = 8;
        int playerInvY = 86;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }

        // Хотбар игрока
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, playerInvX + i * 18, playerInvY + 58));
        }
    }

    // Метод для получения инвентаря аксессуаров, нужен для AccessoryScreen
    public AccessoryInventory getAccessoryInventory() {
        return this.accessoryInventory;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        // Логика для Shift-клика
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        // Перемещение из инвентаря аксессуаров в инвентарь игрока
        if (index < SLOTS) {
            if (!this.moveItemStackTo(sourceStack, SLOTS, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }
        // Перемещение из инвентаря игрока в инвентарь аксессуаров
        else {
            // TODO: В будущем добавить проверку, подходит ли предмет для слота
            if (!this.moveItemStackTo(sourceStack, 0, SLOTS, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}