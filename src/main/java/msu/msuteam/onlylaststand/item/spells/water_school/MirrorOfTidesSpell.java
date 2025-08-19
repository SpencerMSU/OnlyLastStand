package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MirrorOfTidesSpell extends SpellItem {
    public static final int DURATION_TICKS = 120; // 6 сек

    public MirrorOfTidesSpell(Properties p) {
        super(p, Rarity.LEGENDARY, 70, 700); // 70 маны, 35 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        // Упрощенная версия: общий резист (как щит воды), +абсорбция
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, DURATION_TICKS, 0, false, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, DURATION_TICKS, 1, false, true, true));
        level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.4F);
    }
}