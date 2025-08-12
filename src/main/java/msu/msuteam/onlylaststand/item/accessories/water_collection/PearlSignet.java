package msu.msuteam.onlylaststand.item.accessories.water_collection;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class PearlSignet extends AccessoryItem {
    private static final ResourceLocation LUCK_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "pearl_signet_luck");

    public PearlSignet(Properties pProperties) {
        super(pProperties, SlotType.SIGNET, CollectionType.WATER);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.LUCK,
                new AttributeModifier(LUCK_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), // +1 Удачи
                EquipmentSlotGroup.ANY
        ).build();
    }
}