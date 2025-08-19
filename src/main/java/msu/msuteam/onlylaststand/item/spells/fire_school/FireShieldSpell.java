package msu.msuteam.onlylaststand.item.spells.fire_school;

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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FireShieldSpell extends SpellItem {

    public static final int DURATION_TICKS = 400; // 20 секунд

    public FireShieldSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 50, 800); // Стоимость: 50 маны, КД: 40 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        player.getPersistentData().putInt("FireShieldTicks", DURATION_TICKS);
        level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void onPlayerTick(Player player) {
        if (player.getPersistentData().contains("FireShieldTicks")) {
            int ticksRemaining = player.getPersistentData().getInt("FireShieldTicks");
            if (ticksRemaining > 0) {
                if (player.level() instanceof ServerLevel serverLevel) {
                    // ИЗМЕНЕНО: Скорость вращения увеличена (множитель 3 -> 4.5)
                    double angle = (player.tickCount % 80) * 4.5 * (Math.PI / 180);

                    for (int i = 0; i < 3; i++) {
                        double currentAngle = angle + (i * 2 * Math.PI / 3);
                        Vec3 spherePos = player.position().add(new Vec3(Math.cos(currentAngle) * 1.5, 0.8, Math.sin(currentAngle) * 1.5));

                        // ИЗМЕНЕНО: Уменьшено количество частиц и добавлена скорость, чтобы они быстро "сгорали"
                        serverLevel.sendParticles(ParticleTypes.FLAME, spherePos.x, spherePos.y, spherePos.z, 2, 0.1, 0.1, 0.1, 0.05);

                        if (player.tickCount % 10 == 0) {
                            AABB damageArea = new AABB(BlockPos.containing(spherePos)).inflate(0.5);
                            for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, damageArea, e -> e != player && e.isAlive())) {
                                entity.hurt(player.level().damageSources().indirectMagic(player, player), 2.0F); // 1 сердце
                            }
                        }
                    }
                }
                player.getPersistentData().putInt("FireShieldTicks", ticksRemaining - 1);
            } else {
                player.getPersistentData().remove("FireShieldTicks");
            }
        }
    }
}