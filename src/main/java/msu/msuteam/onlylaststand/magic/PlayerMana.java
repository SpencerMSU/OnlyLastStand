package msu.msuteam.onlylaststand.magic;

import msu.msuteam.onlylaststand.network.SyncManaPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;

public class PlayerMana implements INBTSerializable<CompoundTag> {

    private float currentMana;
    private float maxMana;
    private transient Player player; // Ссылка на игрока, не сохраняется

    // Базовое значение маны по умолчанию
    private static final float DEFAULT_MAX_MANA = 300.0f;
    public static final float DEFAULT_MANA_REGEN = 5.0f / 20.0f;

    public PlayerMana() {
        this.maxMana = DEFAULT_MAX_MANA;
        this.currentMana = DEFAULT_MAX_MANA;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getCurrentMana() { return currentMana; }
    public float getMaxMana() { return maxMana; }

    public void setCurrentMana(float amount) {
        float oldMana = this.currentMana;
        this.currentMana = Mth.clamp(amount, 0, this.maxMana);
        if (oldMana != this.currentMana && player instanceof ServerPlayer) {
            sync();
        }
    }

    public void setMaxMana(float amount) {
        float oldMax = this.maxMana;
        this.maxMana = Math.max(1, amount);
        if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        }
        if (oldMax != this.maxMana && player instanceof ServerPlayer) {
            sync();
        }
    }

    public void consume(float amount) {
        this.setCurrentMana(this.currentMana - amount);
    }

    public void regenerate(float amount) {
        this.setCurrentMana(this.currentMana + amount);
    }

    public void sync() {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(this.currentMana, this.maxMana));
        }
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
        // Если значения отсутствуют в NBT, используем корректные дефолты
        float loadedMax = nbt.contains("MaxMana") ? nbt.getFloat("MaxMana") : DEFAULT_MAX_MANA;
        float loadedCurrent = nbt.contains("CurrentMana") ? nbt.getFloat("CurrentMana") : loadedMax;

        this.maxMana = Math.max(1, loadedMax);
        this.currentMana = Mth.clamp(loadedCurrent, 0, this.maxMana);
    }
}