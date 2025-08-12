package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AccessoryInventory extends ItemStackHandler {
    public static final int SLOTS = 9;

    public AccessoryInventory() {
        super(SLOTS);
    }

    @Override
    protected void onContentsChanged(int slot) {
        // Этот метод можно будет использовать для обновления атрибутов игрока в будущем
        // OnlyLastStand.LOGGER.info("Accessory in slot " + slot + " changed!");
    }
}