package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.Vec3;

public class FlameDashSpell extends SpellItem {
    public FlameDashSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 25, 160); // 8 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 lookAngle = player.getLookAngle();
        player.setDeltaMovement(lookAngle.scale(1.8).add(0, 0.2, 0));
        player.hurtMarked = true; // Для синхронизации движения

        if (level instanceof ServerLevel serverLevel) {
            // Эффектный след из частиц
            for (int i = 0; i < 8; i++) {
                Vec3 trailPos = player.position().subtract(lookAngle.scale(i * 0.5));
                serverLevel.sendParticles(ParticleTypes.FLAME, trailPos.x, trailPos.y + 1, trailPos.z, 20, 0.3, 0.3, 0.3, 0);
            }
            // Поджигаем землю
            BlockPos playerPos = player.blockPosition().below();
            if (level.getBlockState(playerPos).isSolid() && level.isEmptyBlock(playerPos.above())) {
                level.setBlockAndUpdate(playerPos.above(), BaseFireBlock.getState(level, playerPos.above()));
            }
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.8F, 0.8F);
    }
}