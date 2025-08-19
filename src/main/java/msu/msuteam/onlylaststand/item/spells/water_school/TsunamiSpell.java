package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TsunamiSpell extends SpellItem {
    public TsunamiSpell(Properties p) {
        super(p, Rarity.MYTHIC, 120, 1500); // 120 маны, 75 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 origin = player.position().add(0, 0.2, 0);
        Vec3 dir = player.getLookAngle().normalize();

        if (level instanceof ServerLevel sl) {
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.3F, 0.7F);

            int steps = 24;
            double spread = Math.toRadians(40); // полуконус
            for (int s = 1; s <= steps; s++) {
                double t = s / (double) steps;
                double dist = 1 + t * 15;
                for (int k = -8; k <= 8; k++) {
                    double ang = (k / 8.0) * (spread / 2.0);
                    Vec3 side = new Vec3(dir.x * Math.cos(ang) - dir.z * Math.sin(ang), dir.y, dir.z * Math.cos(ang) + dir.x * Math.sin(ang)).normalize();
                    Vec3 p = origin.add(side.scale(dist));
                    sl.sendParticles(ParticleTypes.SPLASH, p.x, p.y, p.z, 2, 0.1, 0.1, 0.1, 0.02);
                    sl.sendParticles(ParticleTypes.CLOUD, p.x, p.y + 0.2, p.z, 1, 0.0, 0.0, 0.0, 0.0);

                    for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(BlockPos.containing(p)).inflate(1.2), e -> e != player && e.isAlive())) {
                        e.hurt(level.damageSources().indirectMagic(player, player), 10.0F);
                        Vec3 knock = side.scale(1.2).add(0, 0.2, 0);
                        e.push(knock.x, knock.y, knock.z);
                        e.clearFire();
                    }
                }
            }
        }
    }
}