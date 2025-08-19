package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DragonsBreathSpell extends SpellItem {
    public DragonsBreathSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 30, 100); // Стоимость: 30 маны, КД: 5 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 look = player.getLookAngle();
            Vec3 start = player.getEyePosition();

            // Создаем частицы в форме конуса
            for (int i = 0; i < 200; i++) {
                double distance = level.random.nextDouble() * 6.0;
                double angle = level.random.nextDouble() * 2 * Math.PI;
                double coneRatio = 0.4; // Чем больше, тем шире конус

                Vec3 randomVec = new Vec3(Math.cos(angle) * coneRatio, Math.sin(angle) * coneRatio, 1.0)
                        .xRot((float) (level.random.nextFloat() * Mth.PI / 8))
                        .yRot((float) (level.random.nextFloat() * Mth.PI / 8))
                        .normalize();

                Vec3 particleVec = start.add(look.scale(distance)).add(randomVec.scale(distance * coneRatio));
                serverLevel.sendParticles(ParticleTypes.FLAME, particleVec.x, particleVec.y, particleVec.z, 1, 0, 0, 0, 0);
            }

            // Наносим урон в области
            AABB coneArea = player.getBoundingBox().inflate(3.0).move(look.scale(3.0));
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, coneArea, e -> e != player && e.isAlive())) {
                Vec3 toEntity = entity.position().subtract(start).normalize();
                if (toEntity.dot(look) > 0.8) { // Проверяем, что моб в конусе
                    entity.hurt(level.damageSources().indirectMagic(player, player), 10.0F); // 5 сердец
                }
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.2F);
    }
}