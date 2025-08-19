package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SirensAriaSpell extends SpellItem {
    public SirensAriaSpell(Properties p) {
        super(p, Rarity.DEMONIC, 110, 1400); // 110 маны, 70 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        double r = 10.0;
        int dur = 80; // 4 сек

        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(r), e -> e != player && e.isAlive())) {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur, 2, false, true, true));
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, dur, 1, false, true, true));
        }

        if (level instanceof ServerLevel sl) {
            for (int i = 0; i < 30; i++) {
                double a = (Math.PI * 2) * (i / 30.0);
                double x = player.getX() + Math.cos(a) * 3;
                double z = player.getZ() + Math.sin(a) * 3;
                sl.sendParticles(ParticleTypes.NOTE, x, player.getY() + 1.2, z, 1, 0, 0, 0, 0);
                sl.sendParticles(ParticleTypes.ENCHANT, x, player.getY() + 1.0, z, 1, 0, 0, 0, 0);
            }
        }
        // .value() на SOUND
        level.playSound(null, player.blockPosition(), SoundEvents.MUSIC_DISC_FAR.value(), SoundSource.PLAYERS, 0.8F, 1.2F);
    }
}