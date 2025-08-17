package msu.msuteam.onlylaststand.skills;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class PlayerLearnedSpells implements INBTSerializable<CompoundTag> {

    private final Set<ResourceLocation> learnedSpells = new HashSet<>();

    public boolean hasLearned(Item spellItem) {
        return learnedSpells.contains(BuiltInRegistries.ITEM.getKey(spellItem));
    }

    public void learnSpell(Item spellItem) {
        learnedSpells.add(BuiltInRegistries.ITEM.getKey(spellItem));
    }

    public void forgetSpell(Item spellItem) {
        learnedSpells.remove(BuiltInRegistries.ITEM.getKey(spellItem));
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        learnedSpells.forEach(loc -> list.add(StringTag.valueOf(loc.toString())));
        nbt.put("learned", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        learnedSpells.clear();
        if (nbt.contains("learned", 9)) {
            ListTag list = nbt.getList("learned", 8);
            for (int i = 0; i < list.size(); i++) {
                learnedSpells.add(ResourceLocation.parse(list.getString(i)));
            }
        }
    }
}