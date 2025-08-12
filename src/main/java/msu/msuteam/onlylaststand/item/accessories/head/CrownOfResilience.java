package msu.msuteam.onlylaststand.item.accessories.head;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.SlotType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class CrownOfResilience extends AccessoryItem {
    private static final ResourceLocation KB_RESISTANCE_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "crown_kb_resistance");
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(c -> c.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.kb_resistance", "+10%"));
    }
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