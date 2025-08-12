package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AccessorySlot extends SlotItemHandler {
    private final SlotType slotType;

    public AccessorySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, SlotType slotType) {
        super(itemHandler, index, xPosition, yPosition);
        this.slotType = slotType;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof AccessoryItem accessory) {
            return accessory.getSlotType() == this.slotType || accessory.getSlotType() == SlotType.ANY;
        }
        return false;
    }
}