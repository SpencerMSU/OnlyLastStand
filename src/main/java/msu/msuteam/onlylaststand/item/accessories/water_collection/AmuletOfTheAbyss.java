package msu.msuteam.onlylaststand.item.accessories.water_collection;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.NeoForgeMod; // ИСПРАВЛЕНО

public class AmuletOfTheAbyss extends AccessoryItem {
    private static final ResourceLocation SWIM_SPEED_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "abyss_amulet_swim_speed");

    public AmuletOfTheAbyss(Properties pProperties) {
        super(pProperties, SlotType.NECK, CollectionType.WATER);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                NeoForgeMod.SWIM_SPEED, // ИСПРАВЛЕНО
                new AttributeModifier(SWIM_SPEED_ID, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                EquipmentSlotGroup.ANY
        ).build();
    }
}