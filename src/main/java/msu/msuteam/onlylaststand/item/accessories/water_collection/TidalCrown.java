package msu.msuteam.onlylaststand.item.accessories.water_collection;

import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TidalCrown extends AccessoryItem {
    public TidalCrown(Properties pProperties) {
        super(pProperties, SlotType.HEAD, CollectionType.WATER);
    }

    @Override
    public void onAccessoryTick(ItemStack stack, Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 0, false, false, true));
    }
}