package msu.msuteam.onlylaststand.item.spells.water_school;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TidalStepSpell extends SpellItem {
    public TidalStepSpell(Properties p) {
        super(p, Rarity.COMMON, 25, 160); // 25 маны, 8 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.position();
        Vec3 dir = player.getLookAngle().normalize();
        double range = player.isInWater() ? 8.0 : 6.0;

        Vec3 chosen = start;
        for (double t = 0.5; t <= range; t += 0.5) {
            Vec3 p = start.add(dir.scale(t));
            BlockPos pos = BlockPos.containing(p);
            BlockState state = level.getBlockState(pos);
            // В 1.21.1 нет getMaterial(); используем пустую коллизию как «проходимо»
            boolean passable = state.getCollisionShape(level, pos).isEmpty();
            if (passable) {
                chosen = p;
            } else {
                break;
            }
        }

        if (level instanceof ServerLevel sl) {
            for (int i = 0; i < 15; i++) {
                double f = i / 15.0;
                Vec3 p = start.lerp(chosen, f);
                sl.sendParticles(ParticleTypes.DRIPPING_WATER, p.x, p.y + 0.1, p.z, 2, 0.1, 0.1, 0.1, 0);
                sl.sendParticles(ParticleTypes.CLOUD, p.x, p.y + 0.05, p.z, 1, 0.0, 0.0, 0.0, 0);
            }
        }

        player.teleportTo(chosen.x, chosen.y, chosen.z);
        level.playSound(null, player.blockPosition(), SoundEvents.DOLPHIN_HURT, SoundSource.PLAYERS, 0.7F, 1.8F);
    }
}