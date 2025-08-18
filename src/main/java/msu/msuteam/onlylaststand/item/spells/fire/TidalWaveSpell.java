package msu.msuteam.onlylaststand.item.spells.water;

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

public class TidalWaveSpell extends SpellItem {
    public TidalWaveSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 25, 200); // 10 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 lookAngle = player.getLookAngle();
            Vec3 startPos = player.getEyePosition();

            for (int d = 1; d < 8; d++) { // Дистанция волны
                for (int w = -3; w <= 3; w++) { // Ширина волны
                    Vec3 perpendicular = new Vec3(-lookAngle.z, 0, lookAngle.x).normalize();
                    Vec3 particlePos = startPos.add(lookAngle.scale(d)).add(perpendicular.scale(w * 0.5));

                    serverLevel.sendParticles(ParticleTypes.SPLASH, particlePos.x, particlePos.y, particlePos.z, 5, 0.2, 0.2, 0.2, 0);
                }
            }

            AABB waveBox = player.getBoundingBox().move(lookAngle.scale(4)).inflate(3.0, 1.0, 3.0);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, waveBox, e -> e != player && e.isAlive())) {
                Vec3 knockback = lookAngle.scale(1.5).add(0, 0.3, 0);
                entity.setDeltaMovement(knockback);
                entity.hurt(level.damageSources().indirectMagic(player, player), 3.0F);
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_SWIM, SoundSource.PLAYERS, 1.0F, 0.8F);
    }
}