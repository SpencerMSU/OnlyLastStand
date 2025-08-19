package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DevilsToolsSpell extends SpellItem {
    public static final int DURATION_TICKS = 1200; // 1 минута

    public DevilsToolsSpell(Properties pProperties) {
        super(pProperties, Rarity.LEGENDARY, 100, 12000); // КД 10 минут
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        player.getPersistentData().putInt("DevilsToolsTicks", DURATION_TICKS);
        // +200% скорости это как +2.0 к базовой, что эквивалентно 10 уровням Haste.
        // Даем Haste 10 для очень быстрого копания.
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, DURATION_TICKS, 9, false, true));
        level.playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.7F, 1.5F);
    }
}