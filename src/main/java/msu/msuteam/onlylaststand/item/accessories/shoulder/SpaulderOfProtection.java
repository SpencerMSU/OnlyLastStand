package msu.msuteam.onlylaststand.item.accessories.shoulder;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SpaulderOfProtection extends AccessoryItem {
    private static final ResourceLocation ARMOR_TOUGHNESS_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "spaulder_armor_toughness");

    public SpaulderOfProtection(Properties pProperties) {
        super(pProperties, SlotType.LEFT_SHOULDER);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(ARMOR_TOUGHNESS_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), // +1 прочности
                EquipmentSlotGroup.ANY
        ).build();
    }
}