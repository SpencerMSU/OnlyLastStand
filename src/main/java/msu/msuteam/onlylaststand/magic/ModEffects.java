package msu.msuteam.onlylaststand.magic;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder; // <-- ИЗМЕНЕН ИМПОРТ
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, OnlyLastStand.MODID);

    // ИСПРАВЛЕНО: Указываем правильный тип DeferredHolder
    public static final DeferredHolder<MobEffect, MobEffect> MANA_REGENERATION = EFFECTS.register("mana_regeneration",
            ManaRegenEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}