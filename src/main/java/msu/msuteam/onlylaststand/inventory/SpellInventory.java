package msu.msuteam.onlylaststand.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;

// Аналогичен AccessoryInventory, но на 10 слотов
public class SpellInventory extends ItemStackHandler {
    public static final int SLOTS = 10;

    public SpellInventory() {
        super(SLOTS);
    }

    // Переопределяем методы сериализации с новыми сигнатурами
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return super.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
    }
}