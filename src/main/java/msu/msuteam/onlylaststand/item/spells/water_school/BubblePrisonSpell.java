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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BubblePrisonSpell extends SpellItem {
    public BubblePrisonSpell(Properties p) {
        super(p, Rarity.RARE, 35, 280); // 35 маны, 14 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        Vec3 end = start.add(dir.scale(15));
        BlockHitResult block = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
        double maxDist = block.getLocation().distanceTo(start);

        LivingEntity target = null;
        for (double d = 0; d < maxDist; d += 0.25) {
            Vec3 pos = start.add(dir.scale(d));
            AABB box = new AABB(pos.x - 0.6, pos.y - 0.6, pos.z - 0.6, pos.x + 0.6, pos.y + 0.6, pos.z + 0.6);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
                target = e;
                d = maxDist;
                break;
            }
        }

        if (target != null) {
            int dur = 60;
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, dur, 0, false, true, true));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur, 2, false, true, true));
            target.clearFire();

            if (level instanceof ServerLevel sl) {
                for (int i = 0; i < 40; i++) {
                    double a = (Math.PI * 2) * (i / 40.0);
                    double x = target.getX() + Math.cos(a) * 0.8;
                    double y = target.getY() + 0.8 + Math.sin(a * 3) * 0.2;
                    double z = target.getZ() + Math.sin(a) * 0.8;
                    sl.sendParticles(ParticleTypes.BUBBLE, x, y, z, 1, 0, 0, 0, 0.02);
                }
            }
            level.playSound(null, target.blockPosition(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.PLAYERS, 0.7F, 1.2F);
        }
    }
}