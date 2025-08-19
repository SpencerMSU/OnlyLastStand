package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HelpFromHellSpell extends SpellItem {
    public HelpFromHellSpell(Properties pProperties) {
        super(pProperties, Rarity.LEGENDARY, 150, 144000); // КД 2 часа
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        // Чиним броню
        player.getArmorSlots().forEach(itemStack -> {
            if (itemStack.isDamageableItem()) {
                itemStack.setDamageValue(0);
            }
        });
        // Чиним предмет в основной руке
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.isDamageableItem()) {
            mainHandItem.setDamageValue(0);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}