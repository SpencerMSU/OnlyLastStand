package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireboltSpell extends SpellItem {
    public FireboltSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 10, 20);
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 lookAngle = player.getLookAngle();

        // ИСПРАВЛЕНО: Используем правильный конструктор, который принимает игрока и вектор направления.
        SmallFireball fireball = new SmallFireball(level, player, lookAngle);

        // Смещаем позицию спавна, чтобы фаербол не появлялся внутри игрока
        fireball.setPos(player.getX() + lookAngle.x * 0.5, player.getEyeY() - 0.2, player.getZ() + lookAngle.z * 0.5);

        level.addFreshEntity(fireball);
        level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.7F, 1.2F);
    }
}