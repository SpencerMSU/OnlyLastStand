package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RippleDartSpell extends SpellItem {
    public RippleDartSpell(Properties p) {
        super(p, Rarity.COMMON, 20, 120); // 20 маны, 6 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        Vec3 end = start.add(dir.scale(30));

        BlockHitResult block = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
        double maxDist = block.getLocation().distanceTo(start);

        // Линейный «пирсинг»: ищем первую цель
        LivingEntity hit = null;
        double step = 0.5;
        for (double d = 0; d < maxDist; d += step) {
            Vec3 pos = start.add(dir.scale(d));
            AABB box = new AABB(pos.x - 0.5, pos.y - 0.5, pos.z - 0.5, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
                hit = e;
                d = maxDist;
                break;
            }
            if (level instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.SPLASH, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
                sl.sendParticles(ParticleTypes.BUBBLE, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0.01);
            }
        }

        if (hit != null) {
            hit.hurt(level.damageSources().indirectMagic(player, player), 6.0F);
            // Снимаем поджог, накладываем лёгкий дизориент
            hit.clearFire();
            hit.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.UNLUCK, 40, 0, false, true, true));
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.PLAYERS, 0.8F, 1.2F);
    }
}