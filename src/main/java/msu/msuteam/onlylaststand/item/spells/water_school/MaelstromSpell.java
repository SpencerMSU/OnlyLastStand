package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class MaelstromSpell extends SpellItem {
    public MaelstromSpell(Properties p) {
        super(p, Rarity.EPIC, 60, 500); // 60 маны, 25 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        Vec3 end = start.add(dir.scale(16));
        BlockHitResult block = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
        Vec3 center = block.getLocation();

        double radius = 4.0;
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(BlockPos.containing(center)).inflate(radius), e -> e != player && e.isAlive())) {
            Vec3 pull = center.subtract(e.position()).normalize().scale(0.6);
            e.push(pull.x, 0.1, pull.z);
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, true, true));
            e.hurt(level.damageSources().indirectMagic(player, player), 4.0F);
        }

        if (level instanceof ServerLevel sl) {
            for (int i = 0; i < 120; i++) {
                double a = (Math.PI * 2) * (i / 60.0);
                double rr = radius * (i % 2 == 0 ? 1.0 : 0.7);
                double x = center.x + Math.cos(a) * rr;
                double z = center.z + Math.sin(a) * rr;
                sl.sendParticles(ParticleTypes.BUBBLE, x, center.y + 0.2, z, 1, 0, 0, 0, 0.03);
            }
        }
        // В твоих маппингах это уже SoundEvent, без .value()
        level.playSound(null, BlockPos.containing(center), SoundEvents.CONDUIT_AMBIENT, SoundSource.PLAYERS, 0.8F, 0.9F);
    }
}