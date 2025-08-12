package msu.msuteam.onlylaststand.item.accessories;

import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class AccessoryItem extends Item {

    protected final SlotType slotType;

    public AccessoryItem(Properties pProperties, SlotType slotType) {
        super(pProperties.component(ModDataComponents.ACCESSORY_LEVEL, 0).component(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON));
        this.slotType = slotType;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    // ИСПРАВЛЕНО: Этот метод теперь вызывается ТОЛЬКО нашим AttributeManager
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.EMPTY; // Базовый аксессуар не дает баффов
    }

    // ИСПРАВЛЕНО: Этот стандартный метод теперь ВСЕГДА возвращает пустоту,
    // чтобы игра не применяла баффы, когда предмет в руке.
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.EMPTY;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;

        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.rarity", rarity.getDisplayName()));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.level", level));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.slot", this.slotType.getDisplayName()));

        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buffs_placeholder"));
    }
}