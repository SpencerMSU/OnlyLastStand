package msu.msuteam.onlylaststand.item.accessories;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
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

public class SpeedRingItem extends AccessoryItem {

    private static final ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "speed_ring_bonus");

    public SpeedRingItem(Properties pProperties) {
        super(pProperties, SlotType.SIGNET);
    }

    // ИСПРАВЛЕНО: Теперь мы переопределяем наш кастомный метод
    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        Rarity rarity = stack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = stack.get(ModDataComponents.ACCESSORY_LEVEL);

        if (rarity == null) rarity = Rarity.COMMON;

        double speedBonus = (0.04 + (level * 0.01)) * rarity.getMultiplier();

        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.MOVEMENT_SPEED,
                        new AttributeModifier(SPEED_MODIFIER_ID, speedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.ANY
                )
                .build();
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(component -> component.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));

        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;

        double speedBonus = (0.04 + (level * 0.01)) * rarity.getMultiplier();
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.speed", String.format("%.0f%%", speedBonus * 100)));
    }
}