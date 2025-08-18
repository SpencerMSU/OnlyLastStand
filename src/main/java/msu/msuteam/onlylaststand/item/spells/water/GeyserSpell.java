package msu.msuteam.onlylaststand.item.spells.water;

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

public class GeyserSpell extends SpellItem {
    public GeyserSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 35, 400); // 20 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(20)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 targetPos = ray.getLocation();
        BlockPos blockPos = BlockPos.containing(targetPos);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, targetPos.x, targetPos.y, targetPos.z, 50, 0.5, 0.1, 0.5, 0.1);
            level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.PLAYERS, 1.0f, 1.0f);

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                serverLevel.getServer().execute(() -> {
                    level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.PLAYERS, 1.5F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.SPLASH, targetPos.x, targetPos.y, targetPos.z, 200, 0.5, 1.0, 0.5, 0.5);
                    // ИСПРАВЛЕНО: Используем BlockPos для AABB
                    AABB area = new AABB(blockPos).inflate(1.5, 0, 1.5);
                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive())) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1.8, 0));
                    }
                });
            }, 1, TimeUnit.SECONDS);
        }
    }
}