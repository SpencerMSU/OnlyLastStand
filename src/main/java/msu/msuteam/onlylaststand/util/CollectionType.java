package msu.msuteam.onlylaststand.util;

import net.minecraft.network.chat.Component;

public enum CollectionType {
    NONE("Нет", 0),
    FIRE("Огненная", 70),
    WATER("Водная", 30);

    private final String name;
    private final int weight;

    CollectionType(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public Component getDisplayName() {
        return Component.translatable("collection.onlylaststand." + this.name().toLowerCase());
    }

    public int getWeight() {
        return weight;
    }
}