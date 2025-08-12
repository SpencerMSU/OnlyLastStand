package msu.msuteam.onlylaststand.item.accessories.neck;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class AmuletOfVitality extends AccessoryItem {
    private static final ResourceLocation HEALTH_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "amulet_health");

    public AmuletOfVitality(Properties pProperties) {
        super(pProperties, SlotType.NECK);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.MAX_HEALTH,
                new AttributeModifier(HEALTH_ID, 2.0, AttributeModifier.Operation.ADD_VALUE), // +2 HP (1 сердце)
                EquipmentSlotGroup.ANY
        ).build();
    }
}