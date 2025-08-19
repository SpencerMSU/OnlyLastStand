package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BladesOfFireSpell extends SpellItem {
    public static final int DURATION_TICKS = 1200; // 1 минута

    public BladesOfFireSpell(Properties pProperties) {
        super(pProperties, Rarity.EPIC, 80, 9600); // КД 8 минут
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        int currentTicks = player.getPersistentData().getInt("BladesOfFireTicks");
        player.getPersistentData().putInt("BladesOfFireTicks", currentTicks + DURATION_TICKS);
        level.playSound(null, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 0.8F);
    }
}