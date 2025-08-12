package msu.msuteam.onlylaststand.item.accessories.knee;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class GreavesOfSafety extends AccessoryItem {
    private static final ResourceLocation SAFE_FALL_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "greaves_safe_fall");

    public GreavesOfSafety(Properties pProperties) {
        super(pProperties, SlotType.KNEE_PADS);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        Rarity rarity = stack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = stack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;
        double bonus = (2.0 + (level * 0.5)) * rarity.getMultiplier();
        return ItemAttributeModifiers.builder().add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(SAFE_FALL_ID, bonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY).build();
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(c -> c.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));
        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;
        double bonus = (2.0 + (level * 0.5)) * rarity.getMultiplier();
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.safe_fall", String.format("+%.1f", bonus)));
    }
}