package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class FlameLightSpell extends SpellItem {
    public FlameLightSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 10, 100); // 5 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(20)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 targetPos = ray.getLocation();

        if (level instanceof ServerLevel serverLevel) {
            // Создаем плотное облако ярких частиц, которое будет имитировать свет
            for (int i = 0; i < 50; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
                double offsetY = (level.random.nextDouble() - 0.5) * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, targetPos.x + offsetX, targetPos.y + offsetY, targetPos.z + offsetZ, 1, 0, 0.05, 0, 0);
            }
        }
        level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7F, 1.5F);
    }
}