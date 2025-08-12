package msu.msuteam.onlylaststand.item.accessories.water_collection;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.Rarity;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class FinBracers extends AccessoryItem {
    // ИСПРАВЛЕНО: Заменяем бонус на рабочий - Максимальное здоровье
    private static final ResourceLocation HEALTH_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "fin_bracers_health");

    public FinBracers(Properties pProperties) {
        super(pProperties, SlotType.ELBOW_PADS, CollectionType.WATER);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        Rarity rarity = stack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = stack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;
        // Даем небольшой бонус к здоровью
        double bonus = (1.0 + (level * 0.2)) * rarity.getMultiplier();
        return ItemAttributeModifiers.builder().add(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_ID, bonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY).build();
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(c -> c.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));
        Rarity rarity = pStack.get(ModDataComponents.ACCESSORY_RARITY);
        int level = pStack.get(ModDataComponents.ACCESSORY_LEVEL);
        if (rarity == null) rarity = Rarity.COMMON;
        double bonus = (1.0 + (level * 0.2)) * rarity.getMultiplier();
        // Используем существующий ключ локализации для здоровья
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.health", String.format("+%.1f", bonus)));
    }
}