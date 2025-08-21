package msu.msuteam.onlylaststand.item.accessories;

import com.google.common.collect.Multimap;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.event.PlayerEventHandler;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class AccessoryItem extends Item implements ICurioItem {

    protected final SlotType slotType;
    protected final CollectionType collectionType;

    public AccessoryItem(Properties pProperties, SlotType slotType, CollectionType collectionType) {
        super(pProperties.stacksTo(1).component(ModDataComponents.ACCESSORY_LEVEL, 0).component(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON));
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

    public void onAccessoryTick(ItemStack stack, Player player) {}

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.rarity", rarity.getDisplayName()));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.level", level));
        pTooltipComponents.add(Component.translatable("curios.slot", this.slotType.getDisplayName()));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buffs_placeholder"));
    }

    // --- ИСПРАВЛЕННЫЕ МЕТОДЫ ИНТЕГРАЦИИ С CURIOS ---

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return this.getAccessoryAttributeModifiers(stack).modifiers();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            onAccessoryTick(stack, player);
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && !player.level().isClientSide) {
            PlayerEventHandler.updateAllPlayerModifiers(player);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && !player.level().isClientSide) {
            PlayerEventHandler.updateAllPlayerModifiers(player);
        }
    }
}