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
import net.minecraft.world.phys.AABB;

public class HealingRainSpell extends SpellItem {
    public HealingRainSpell(Properties p) {
        super(p, Rarity.RARE, 40, 360); // 40 маны, 18 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        int dur = 120; // 6 сек
        double r = 6.0;

        AABB box = new AABB(player.blockPosition()).inflate(r);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive())) {
            boolean ally = e == player || (e instanceof Player p && p.isAlliedTo(player));
            if (ally) {
                e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, dur, 0, false, true, true));
                e.clearFire();
            } else if (e.isOnFire()) {
                e.hurt(level.damageSources().indirectMagic(player, player), 2.0F);
            }
        }

        if (level instanceof ServerLevel sl) {
            for (int i = 0; i < 80; i++) {
                double a = Math.random() * Math.PI * 2;
                double R = Math.sqrt(Math.random()) * r;
                double x = player.getX() + Math.cos(a) * R;
                double z = player.getZ() + Math.sin(a) * R;
                double y = player.getY() + 2 + Math.random() * 2;
                sl.sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 1, 0, 0, 0, 0);
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.WEATHER_RAIN, SoundSource.PLAYERS, 0.7F, 1.2F);
    }
}