package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagmaBloodSpell extends SpellItem {
    public MagmaBloodSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 45, 1200); // Стоимость: 45 маны, КД: 60 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        // Даем эффект огнестойкости на 30 секунд
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.LAVA, player.getX(), player.getY() + 1, player.getZ(), 50, 0.5, 0.5, 0.5, 0);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}