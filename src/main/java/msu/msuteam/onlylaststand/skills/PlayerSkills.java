package msu.msuteam.onlylaststand.skills;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkills implements INBTSerializable<CompoundTag> {

    private final Map<PlayerSkill, SkillData> skills = new EnumMap<>(PlayerSkill.class);
    private int unlockedSpellSlots = 1;

    public PlayerSkills() {
        for (PlayerSkill skill : PlayerSkill.values()) {
            skills.put(skill, new SkillData(skill));
        }
    }

    public SkillData getSkill(PlayerSkill skill) {
        return skills.get(skill);
    }

    public int getUnlockedSpellSlots() {
        return unlockedSpellSlots;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        skills.forEach((skill, data) -> nbt.put(skill.name(), data.serializeNBT(provider)));
        nbt.putInt("unlockedSpellSlots", this.unlockedSpellSlots);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (PlayerSkill skill : PlayerSkill.values()) {
            if (nbt.contains(skill.name())) {
                skills.get(skill).deserializeNBT(provider, nbt.getCompound(skill.name()));
            }
        }
        this.unlockedSpellSlots = nbt.contains("unlockedSpellSlots") ? nbt.getInt("unlockedSpellSlots") : 1;
    }

    public class SkillData implements INBTSerializable<CompoundTag> {
        private final PlayerSkill parentSkill;
        private int level = 1;
        private int experience = 0;
        private static final int MAX_LEVEL = 100;

        public SkillData(PlayerSkill parentSkill) {
            this.parentSkill = parentSkill;
        }

        public int getLevel() { return level; }
        public int getExperience() { return experience; }
        public int getXpNeeded() { return (int) (100 * Math.pow(level, 1.5)); }
        public int getMaxLevel() { return MAX_LEVEL; }

        public void addExperience(Player player, int amount) {
            if (level >= MAX_LEVEL) return;
            experience += amount;
            while (experience >= getXpNeeded()) {
                if (level >= MAX_LEVEL) {
                    experience = 0;
                    break;
                }
                experience -= getXpNeeded();
                level++;
                onLevelUp(player, level);
            }
        }

        private void onLevelUp(Player player, int newLevel) {
            if (this.parentSkill == PlayerSkill.MAGIC) {
                PlayerSkills.this.unlockedSpellSlots = 1 + (newLevel / 10);
            }
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("level", level);
            nbt.putInt("experience", experience);
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            this.level = nbt.contains("level") ? nbt.getInt("level") : 1;
            this.experience = nbt.getInt("experience");
        }
    }
}