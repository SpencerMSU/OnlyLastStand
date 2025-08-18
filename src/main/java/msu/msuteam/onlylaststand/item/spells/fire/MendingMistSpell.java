package msu.msuteam.onlylaststand.item.spells.water;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MendingMistSpell extends SpellItem {
    public MendingMistSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 50, 900); // 45 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, player.getX(), player.getY(), player.getZ());
        cloud.setOwner(player);
        cloud.setRadius(3.0F);
        cloud.setDuration(200); // 10 секунд
        cloud.setWaitTime(10);
        cloud.setRadiusPerTick(-0.005F);
        cloud.setParticle(ParticleTypes.WHITE_ASH);
        cloud.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1)); // Регенерация 2 каждые 2 секунды

        level.addFreshEntity(cloud);
        level.playSound(null, player.blockPosition(), SoundEvents.WEATHER_RAIN, SoundSource.PLAYERS, 0.7F, 1.5F);
    }
}