package msu.msuteam.onlylaststand.item.accessories.elbow;
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
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class VambraceOfStrength extends AccessoryItem {
    private static final ResourceLocation DAMAGE_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "vambrace_damage");
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(c -> c.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.damage", "+1"));
    }
    public VambraceOfStrength(Properties pProperties) {
        super(pProperties, SlotType.ELBOW_PADS);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(DAMAGE_ID, 1.0, AttributeModifier.Operation.ADD_VALUE), // +1 урона
                EquipmentSlotGroup.ANY
        ).build();
    }
}