package msu.msuteam.onlylaststand.item.accessories.gloves;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class GauntletsOfAlacrity extends AccessoryItem {
    private static final ResourceLocation ATTACK_SPEED_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "gauntlets_attack_speed");

    public GauntletsOfAlacrity(Properties pProperties) {
        super(pProperties, SlotType.GLOVES);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(ATTACK_SPEED_ID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), // +10%
                EquipmentSlotGroup.ANY
        ).build();
    }
}