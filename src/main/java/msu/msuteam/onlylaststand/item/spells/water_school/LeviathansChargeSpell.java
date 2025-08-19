package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
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

public class LeviathansChargeSpell extends SpellItem {
    public LeviathansChargeSpell(Properties p) {
        super(p, Rarity.LEGENDARY, 85, 900); // 85 маны, 45 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        double length = 20.0;
        double radius = 2.0;

        if (level instanceof ServerLevel sl) {
            for (double d = 0; d < length; d += 0.6) {
                Vec3 p = start.add(dir.scale(d));
                sl.sendParticles(ParticleTypes.CLOUD, p.x, p.y, p.z, 2, 0, 0, 0, 0);
                sl.sendParticles(ParticleTypes.BUBBLE, p.x, p.y, p.z, 3, 0, 0, 0, 0.03);
                AABB tube = new AABB(p.x - radius, p.y - radius, p.z - radius, p.x + radius, p.y + radius, p.z + radius);
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, tube, e -> e != player && e.isAlive())) {
                    e.hurt(level.damageSources().indirectMagic(player, player), 16.0F);
                    Vec3 push = dir.scale(1.0).add(0, 0.2, 0);
                    e.push(push.x, push.y, push.z);
                }
            }
        }
        // .value()
        level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1.0F, 0.9F);
    }
}