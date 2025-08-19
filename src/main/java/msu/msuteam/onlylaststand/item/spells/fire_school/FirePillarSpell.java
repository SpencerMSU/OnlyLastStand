package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FirePillarSpell extends SpellItem {
    public FirePillarSpell(Properties pProperties) {
        super(pProperties, Rarity.EPIC, 70, 600); // Стоимость: 70 маны, КД: 30 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(7)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        BlockPos targetPos;

        if (ray.getType() == HitResult.Type.BLOCK) {
            targetPos = ray.getBlockPos().above();
        } else {
            Vec3 endPoint = player.getEyePosition().add(player.getLookAngle().scale(7));
            targetPos = BlockPos.containing(endPoint.x, level.getHeight(Heightmap.Types.WORLD_SURFACE, (int)endPoint.x, (int)endPoint.z), endPoint.z);
        }

        if (level instanceof ServerLevel serverLevel) {
            double radius = 3.5 / 2.0;
            for (int i = 0; i < 100; i++) {
                double angle = Math.random() * 2 * Math.PI;
                double r = Math.sqrt(Math.random()) * radius;
                double x = targetPos.getX() + 0.5 + Math.cos(angle) * r;
                double z = targetPos.getZ() + 0.5 + Math.sin(angle) * r;
                serverLevel.sendParticles(ParticleTypes.FLAME, x, targetPos.getY() + 0.1, z, 1, 0, 0, 0, 0);
            }

            for (int y = 0; y < 10; y++) {
                serverLevel.sendParticles(ParticleTypes.LAVA, targetPos.getX() + 0.5, targetPos.getY() + y, targetPos.getZ() + 0.5, 30, radius, 0.5, radius, 0);
            }

            AABB pillarArea = new AABB(targetPos).expandTowards(0, 10, 0).inflate(radius, 0, radius);
            // ИСПРАВЛЕНО: Правильная проверка на несколько типов сущностей
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, pillarArea, e -> !(e instanceof EnderDragon))) {
                entity.hurt(level.damageSources().indirectMagic(player, player), 20.0F);
                entity.setRemainingFireTicks(10 * 20);
            }
        }
        // ИСПРАВЛЕНО: Используем .value() для получения SoundEvent
        level.playSound(null, targetPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}