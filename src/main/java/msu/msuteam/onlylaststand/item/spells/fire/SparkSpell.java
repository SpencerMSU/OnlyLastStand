package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SparkSpell extends SpellItem {
    public SparkSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 15, 40);
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 lookAngle = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();

        for (int i = 0; i < 25; i++) {
            double spread = 0.8;
            double offsetX = (level.random.nextDouble() - 0.5) * spread;
            double offsetY = (level.random.nextDouble() - 0.5) * spread;
            double offsetZ = (level.random.nextDouble() - 0.5) * spread;
            double speed = 1.5 + level.random.nextDouble();
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME, eyePos.x, eyePos.y, eyePos.z, 1, lookAngle.x * speed + offsetX, lookAngle.y * speed + offsetY, lookAngle.z * speed + offsetZ, 0);
            }
        }

        AABB area = new AABB(eyePos, eyePos.add(lookAngle.scale(6)));
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, e -> e != player && e.isAlive())) {
            Vec3 toEntity = entity.position().subtract(player.position()).normalize();
            if (toEntity.dot(lookAngle) > 0.7) {
                // ИСПРАВЛЕНО: Используем правильный метод setRemainingFireTicks
                entity.setRemainingFireTicks(4 * 20); // 4 секунды
                entity.hurt(level.damageSources().indirectMagic(player, player), 2.0F);
            }
        }

        level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.2F);
    }
}