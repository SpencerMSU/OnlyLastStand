package msu.msuteam.onlylaststand.item.accessories;

import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class AccessoryItem extends Item {

    protected final SlotType slotType;
    protected final CollectionType collectionType;

    public AccessoryItem(Properties pProperties, SlotType slotType, CollectionType collectionType) {
        super(pProperties.component(ModDataComponents.ACCESSORY_LEVEL, 0).component(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON));
        this.slotType = slotType;
        this.collectionType = collectionType;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.EMPTY;
    }

    public void onAccessoryTick(ItemStack stack, Player player) {
    }

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