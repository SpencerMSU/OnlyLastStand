package msu.msuteam.onlylaststand.item.accessories;

import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class AccessoryItem extends Item {
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        // Теперь этот метод будет вызываться нашим AttributeManager,
        // но не будет применяться автоматически, когда предмет в руке.
        return ItemAttributeModifiers.EMPTY;
    }
    public AccessoryItem(Properties pProperties) {
        super(pProperties.component(ModDataComponents.ACCESSORY_LEVEL, 0).component(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        // Получаем данные из компонентов
        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);

        if (rarity == null) rarity = Rarity.COMMON; // Значение по умолчанию

        // Добавляем информацию в подсказку
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.rarity", rarity.getDisplayName()));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.level", level));
        // В будущем здесь будут баффы
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buffs_placeholder"));
    }
}