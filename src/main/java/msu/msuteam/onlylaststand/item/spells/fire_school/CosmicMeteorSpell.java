package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CosmicMeteorSpell extends SpellItem {

    public CosmicMeteorSpell(Properties pProperties) {
        super(pProperties, Rarity.MYTHIC, 200, 6000); // КД 5 минут
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(
                player.getEyePosition(),
                player.getEyePosition().add(player.getLookAngle().scale(50)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
        Vec3 targetPos = ray.getLocation();
        BlockPos blockPos = BlockPos.containing(targetPos);

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 360; i += 15) {
                double angle = Math.toRadians(i);
                double x = targetPos.x() + 1.5 * Math.cos(angle);
                double z = targetPos.z() + 1.5 * Math.sin(angle);
                serverLevel.sendParticles(ParticleTypes.WITCH, x, targetPos.y() + 0.1, z, 1, 0, 0, 0, 0);
            }

            level.playSound(null, blockPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.0f, 1.5f);

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                serverLevel.getServer().execute(() -> {
                    level.playSound(null, blockPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.0f, 1.5f);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, targetPos.x, targetPos.y, targetPos.z, 5, 2, 2, 2, 0);
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MAGMA_BLOCK.defaultBlockState()), targetPos.x, targetPos.y, targetPos.z, 100, 2, 1, 2, 0.5);
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHERRACK.defaultBlockState()), targetPos.x, targetPos.y, targetPos.z, 100, 2, 1, 2, 0.5);

                    AABB area = new AABB(blockPos).inflate(3.5);
                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
                        entity.hurt(level.damageSources().indirectMagic(player, player), 60.0F);
                    }
                });
            }, 2500, TimeUnit.MILLISECONDS);
        }
    }
}