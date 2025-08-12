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

public class PauldronOfFortitude extends AccessoryItem {
    private static final ResourceLocation ARMOR_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "pauldron_armor");

    public PauldronOfFortitude(Properties pProperties) {
        super(pProperties, SlotType.RIGHT_SHOULDER);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.ARMOR,
                new AttributeModifier(ARMOR_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), // +2 брони
                EquipmentSlotGroup.ANY
        ).build();
    }
}