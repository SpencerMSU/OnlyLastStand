package msu.msuteam.onlylaststand.skills;

import net.minecraft.network.chat.Component;

public enum PlayerSkill {
    COMBAT("Бой", "Увеличивает урон в бою."),
    FARMING("Фермерство", "Повышает шанс удвоить урожай."),
    MINING("Шахтерство", "Повышает шанс удвоить добычу руд."),
    SMITHING("Ремесло", "Снижает затраты опыта на наковальне."),
    CRAFTING("Крафтинг", "Дает шанс создать дополнительный предмет."),
    VITALITY("Живучесть", "Увеличивает максимальное здоровье."),
    MAGIC("Магия", "Снижает затраты маны и открывает новые ячейки заклинаний."),
    ACCURACY("Точность", "Увеличивает скорость натяжения лука."),
    RADIATION_RESISTANCE("Радиация", "Снижает эффект от радиации.");

    private final Component displayName;
    private final Component description;

    PlayerSkill(String displayName, String description) {
        this.displayName = Component.literal(displayName);
        this.description = Component.literal(description);
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDescription() {
        return description;
    }
}