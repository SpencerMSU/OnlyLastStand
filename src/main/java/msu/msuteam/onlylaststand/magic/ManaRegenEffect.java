package msu.msuteam.onlylaststand.magic;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// ИСПРАВЛЕНО: Создаем свой класс для эффекта, так как конструктор MobEffect защищен
public class ManaRegenEffect extends MobEffect {
    public ManaRegenEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x3498DB); // Синий цвет
    }
}