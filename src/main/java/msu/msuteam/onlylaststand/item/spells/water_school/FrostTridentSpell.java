package msu.msuteam.onlylaststand.item.spells.water_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FrostTridentSpell extends SpellItem {
    public FrostTridentSpell(Properties p) {
        super(p, Rarity.EPIC, 55, 400); // 55 маны, 20 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 start = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        double length = 18.0;
        double step = 0.6;

        if (level instanceof ServerLevel sl) {
            // ВАЖНО: .value() чтобы получить SoundEvent
            level.playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RIPTIDE_3.value(), SoundSource.PLAYERS, 0.9F, 1.4F);
            for (double d = 0; d < length; d += step) {
                Vec3 pos = start.add(dir.scale(d));
                sl.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, 2, 0, 0, 0, 0.01);
                AABB hit = new AABB(pos.x - 0.6, pos.y - 0.6, pos.z - 0.6, pos.x + 0.6, pos.y + 0.6, pos.z + 0.6);
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, hit, e -> e != player && e.isAlive())) {
                    float dmg = d < 4 ? 14.0F : (d < 10 ? 9.0F : 5.0F);
                    if (player.isInWater()) dmg *= 1.25F;
                    e.hurt(level.damageSources().indirectMagic(player, player), dmg);
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, true, true));
                }
            }
        }
    }
}