package msu.msuteam.onlylaststand.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum Rarity {
    COMMON("Common", ChatFormatting.GRAY, 1.0f, 10),
    RARE("Rare", ChatFormatting.BLUE, 2.0f, 20),
    EPIC("Epic", ChatFormatting.DARK_PURPLE, 3.0f, 30),
    LEGENDARY("Legendary", ChatFormatting.GOLD, 4.0f, 40),
    MYTHIC("Mythic", ChatFormatting.LIGHT_PURPLE, 5.0f, 50),
    ABSOLUTE("Absolute", ChatFormatting.RED, 8.0f, 80);

    private final String name;
    private final ChatFormatting color;
    private final float multiplier;
    private final int xpValue;

    Rarity(String name, ChatFormatting color, float multiplier, int xpValue) {
        this.name = name;
        this.color = color;
        this.multiplier = multiplier;
        this.xpValue = xpValue;
    }

    public MutableComponent getDisplayName() {
        return Component.translatable("rarity.onlylaststand." + this.name.toLowerCase()).withStyle(this.color);
    }

    public float getMultiplier() {
        return multiplier;
    }

    public int getXpValue() {
        return xpValue;
    }
}