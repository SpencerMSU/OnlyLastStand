package msu.msuteam.onlylaststand.item.accessories.head;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class CrownOfResilience extends AccessoryItem {
    private static final ResourceLocation KB_RESISTANCE_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "crown_kb_resistance");

    public CrownOfResilience(Properties pProperties) {
        super(pProperties, SlotType.HEAD);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(KB_RESISTANCE_ID, 0.1, AttributeModifier.Operation.ADD_VALUE), // +10%
                EquipmentSlotGroup.ANY
        ).build();
    }
}