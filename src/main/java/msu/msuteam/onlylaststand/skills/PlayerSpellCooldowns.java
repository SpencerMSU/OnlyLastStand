package msu.msuteam.onlylaststand.skills;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerSpellCooldowns implements INBTSerializable<CompoundTag> {

    private final Map<ResourceLocation, Integer> readyTickByItem = new HashMap<>();

    public void setReadyTick(Item spellItem, int readyTick) {
        readyTickByItem.put(BuiltInRegistries.ITEM.getKey(spellItem), readyTick);
    }

    public int getRemainingTicks(Level level, Item spellItem) {
        Integer ready = readyTickByItem.get(BuiltInRegistries.ITEM.getKey(spellItem));
        if (ready == null) return 0;
        long now = level.getGameTime();
        return (int) Math.max(0, ready - now);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        readyTickByItem.forEach((key, value) -> tag.putInt(key.toString(), value));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        readyTickByItem.clear();
        for (String k : nbt.getAllKeys()) {
            readyTickByItem.put(ResourceLocation.parse(k), nbt.getInt(k));
        }
    }
}