package msu.msuteam.onlylaststand.magic;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerMana implements INBTSerializable<CompoundTag> {

    private float currentMana;
    private float maxMana;

    private static final float DEFAULT_MAX_MANA = 100.0f;
    public static final float DEFAULT_MANA_REGEN = 5.0f / 20.0f;

    public PlayerMana() {
        this.maxMana = DEFAULT_MAX_MANA;
        this.currentMana = DEFAULT_MAX_MANA;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(float amount) {
        this.currentMana = Mth.clamp(amount, 0, this.maxMana);
    }

    public void setMaxMana(float amount) {
        this.maxMana = Math.max(1, amount);
        if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        }
    }

    public void consume(float amount) {
        this.setCurrentMana(this.currentMana - amount);
    }

    public void regenerate(float amount) {
        this.setCurrentMana(this.currentMana + amount);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("CurrentMana", this.currentMana);
        nbt.putFloat("MaxMana", this.maxMana);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.maxMana = nbt.contains("MaxMana") ? nbt.getFloat("MaxMana") : DEFAULT_MAX_MANA;
        this.currentMana = nbt.getFloat("CurrentMana");
    }
}