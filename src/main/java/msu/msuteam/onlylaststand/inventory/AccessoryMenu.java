package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import msu.msuteam.onlylaststand.util.SlotType;
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

    public AccessoryMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.getData(ModAttachments.ACCESSORY_INVENTORY));
    }

    public AccessoryMenu(int pContainerId, Inventory playerInventory, AccessoryInventory accessoryInventory) {
        super(ModMenuTypes.ACCESSORY_MENU.get(), pContainerId);
        this.accessoryInventory = accessoryInventory;

        final SlotType[] slotOrder = {
                SlotType.HEAD, SlotType.NECK, SlotType.RIGHT_SHOULDER,
                SlotType.LEFT_SHOULDER, SlotType.GLOVES, SlotType.RING_SET,
                SlotType.SIGNET, SlotType.ELBOW_PADS, SlotType.KNEE_PADS
        };

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int GRID_WIDTH = 3 * SLOT_X_SPACING;
        final int START_X = (176 - GRID_WIDTH) / 2;
        final int START_Y = 18;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = j + i * 3;
                addSlot(new AccessorySlot(accessoryInventory, index, START_X + j * SLOT_X_SPACING, START_Y + i * SLOT_Y_SPACING, slotOrder[index]));
            }
        }

        int playerInvX = 8;
        int playerInvY = 86;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, playerInvX + i * 18, playerInvY + 58));
        }
    }

    public AccessoryInventory getAccessoryInventory() {
        return this.accessoryInventory;
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