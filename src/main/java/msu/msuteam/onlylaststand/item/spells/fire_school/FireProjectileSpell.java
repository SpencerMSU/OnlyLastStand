package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireProjectileSpell extends SpellItem {
    public FireProjectileSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 10, 20); // Стоимость: 10 маны, КД: 1 сек
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 lookAngle = player.getLookAngle();
        Vec3 spawnPos = player.getEyePosition().add(lookAngle.scale(0.5));

        // ИСПРАВЛЕНО: Используем новый конструктор для LargeFireball
        LargeFireball fireball = new LargeFireball(level, player, lookAngle, 1);
        fireball.setPos(spawnPos);
        fireball.setDeltaMovement(lookAngle.scale(2.5));

        level.addFreshEntity(fireball);
        level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.7F, 1.5F);
    }
}