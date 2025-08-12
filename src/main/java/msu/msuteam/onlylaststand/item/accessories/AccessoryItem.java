package msu.msuteam.onlylaststand.item.accessories;

import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType; // <-- Добавьте импорт
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class AccessoryItem extends Item {

    protected final SlotType slotType; // <-- Добавлено поле

    public AccessoryItem(Properties pProperties, SlotType slotType) { // <-- Конструктор изменен
        super(pProperties.component(ModDataComponents.ACCESSORY_LEVEL, 0).component(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON));
        this.slotType = slotType; // <-- Присваиваем тип слота
    }

    public SlotType getSlotType() { // <-- Добавлен геттер
        return slotType;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;

        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.rarity", rarity.getDisplayName()));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.level", level));

        // ИСПРАВЛЕНО: Добавляем тип слота в описание
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.slot", this.slotType.getDisplayName()));

        // Этот плейсхолдер будет удален в дочерних классах, таких как SpeedRingItem
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buffs_placeholder"));
    }
}