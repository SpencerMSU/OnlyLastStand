package msu.msuteam.onlylaststand.util;

import net.minecraft.network.chat.Component;

public enum SlotType {
    HEAD("Головные уборы"),
    NECK("Шея"),
    RIGHT_SHOULDER("Правое плечо"),
    LEFT_SHOULDER("Левое плечо"),
    GLOVES("Перчатки"),
    RING_SET("Набор колец"),
    SIGNET("Перстни"),
    ELBOW_PADS("Налокотники"),
    KNEE_PADS("Наколенники"),
    ANY("Любой слот");

    private final String displayName;

    SlotType(String displayName) {
        this.displayName = displayName;
    }

    public Component getDisplayName() {
        return Component.translatable("slot.onlylaststand." + this.name().toLowerCase());
    }
}