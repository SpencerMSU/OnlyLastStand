package msu.msuteam.onlylaststand.item.accessories.knee;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class GreavesOfSafety extends AccessoryItem {
    private static final ResourceLocation SAFE_FALL_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "greaves_safe_fall");

    public GreavesOfSafety(Properties pProperties) {
        super(pProperties, SlotType.KNEE_PADS);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.SAFE_FALL_DISTANCE,
                new AttributeModifier(SAFE_FALL_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), // +2 блока
                EquipmentSlotGroup.ANY
        ).build();
    }
}