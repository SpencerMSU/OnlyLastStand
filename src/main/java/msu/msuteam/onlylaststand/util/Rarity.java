package msu.msuteam.onlylaststand.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum Rarity {
    COMMON("Common", ChatFormatting.GRAY, 1.0f),
    RARE("Rare", ChatFormatting.BLUE, 2.0f),
    EPIC("Epic", ChatFormatting.DARK_PURPLE, 3.0f),
    LEGENDARY("Legendary", ChatFormatting.GOLD, 4.0f),
    MYTHIC("Mythic", ChatFormatting.LIGHT_PURPLE, 5.0f),
    ABSOLUTE("Absolute", ChatFormatting.RED, 8.0f);

    private final String name;
    private final ChatFormatting color;
    private final float multiplier;

    Rarity(String name, ChatFormatting color, float multiplier) {
        this.name = name;
        this.color = color;
        this.multiplier = multiplier;
    }

    public MutableComponent getDisplayName() {
        return Component.translatable("rarity.onlylaststand." + this.name.toLowerCase()).withStyle(this.color);
    }

    public float getMultiplier() {
        return multiplier;
    }
}