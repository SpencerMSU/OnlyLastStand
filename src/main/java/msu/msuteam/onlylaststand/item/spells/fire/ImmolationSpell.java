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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ImmolationSpell extends SpellItem {

    private static final int DURATION_TICKS = 200; // 10 секунд
    private static final double RADIUS = 3.0;

    public ImmolationSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 40, 600); // 30 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        player.getPersistentData().putInt("ImmolationTicks", DURATION_TICKS);
        level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void onPlayerTick(Player player) {
        if (player.getPersistentData().contains("ImmolationTicks")) {
            int ticksRemaining = player.getPersistentData().getInt("ImmolationTicks");
            if (ticksRemaining > 0) {
                if (player.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 5; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double x = player.getX() + Math.cos(angle) * RADIUS;
                        double z = player.getZ() + Math.sin(angle) * RADIUS;
                        serverLevel.sendParticles(ParticleTypes.FLAME, x, player.getY() + 1, z, 1, 0, 0.1, 0, 0);
                    }
                }

                if (ticksRemaining % 20 == 0) {
                    // ИСПРАВЛЕНО: Используем BlockPos для AABB
                    AABB area = new AABB(player.blockPosition()).inflate(RADIUS);
                    for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e != player && e.isAlive())) {
                        // ИСПРАВЛЕНО: Используем правильный метод setRemainingFireTicks
                        entity.setRemainingFireTicks(5 * 20);
                        entity.hurt(player.level().damageSources().indirectMagic(player, player), 2.0F);
                    }
                }

                player.getPersistentData().putInt("ImmolationTicks", ticksRemaining - 1);
            } else {
                player.getPersistentData().remove("ImmolationTicks");
            }
        }
    }
}