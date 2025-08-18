package msu.msuteam.onlylaststand.item.spells.fire;

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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MeteorSpell extends SpellItem {

    public MeteorSpell(Properties pProperties) {
        super(pProperties, Rarity.LEGENDARY, 60, 800); // 40 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(50)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 targetPos = ray.getLocation();
        BlockPos blockPos = BlockPos.containing(targetPos);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.LAVA, targetPos.x, targetPos.y + 0.2, targetPos.z, 20, 0.5, 0.1, 0.5, 0);
            level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.5f, 2.0f);

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                serverLevel.getServer().execute(() -> {
                    level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.5F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, targetPos.x, targetPos.y, targetPos.z, 2, 1, 1, 1, 0);
                    // ИСПРАВЛЕНО: Используем BlockPos для AABB
                    AABB area = new AABB(blockPos).inflate(4.0);
                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive())) {
                        entity.hurt(level.damageSources().indirectMagic(player, player), 12.0F);
                        // ИСПРАВЛЕНО: Используем правильный метод setRemainingFireTicks
                        entity.setRemainingFireTicks(8 * 20);
                    }
                });
            }, 2, TimeUnit.SECONDS);
        }
    }
}