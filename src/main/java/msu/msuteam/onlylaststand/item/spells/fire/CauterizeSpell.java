package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CauterizeSpell extends SpellItem {
    public CauterizeSpell(Properties pProperties) {
        super(pProperties, Rarity.RARE, 30, 300); // 15 сек кд
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        // Наносим урон и лечим
        player.hurt(level.damageSources().magic(), 2.0F); // 1 сердце
        player.heal(6.0F); // 3 сердца

        // Снимаем негативные эффекты
        player.removeEffect(MobEffects.POISON);
        player.removeEffect(MobEffects.WITHER);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 1, player.getZ(), 50, 0.5, 0.5, 0.5, 0.1);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1, player.getZ(), 25, 0.5, 0.5, 0.5, 1);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 0.7F);
    }
}