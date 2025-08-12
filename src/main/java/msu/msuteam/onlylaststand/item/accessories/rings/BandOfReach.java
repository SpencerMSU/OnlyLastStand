package msu.msuteam.onlylaststand.item.accessories.rings;
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

public class BandOfReach extends AccessoryItem {
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.removeIf(c -> c.equals(Component.translatable("tooltip.onlylaststand.buffs_placeholder")));
        pTooltipComponents.add(Component.translatable("tooltip.onlylaststand.buff.reach", "+0.5"));
    }
    private static final ResourceLocation BLOCK_REACH_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "band_block_reach");
    private static final ResourceLocation ENTITY_REACH_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "band_entity_reach");

    public BandOfReach(Properties pProperties) {
        super(pProperties, SlotType.RING_SET);
    }

    @Override
    public ItemAttributeModifiers getAccessoryAttributeModifiers(ItemStack stack) {
        // ИСПРАВЛЕНО: Теперь мы добавляем оба атрибута
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.BLOCK_INTERACTION_RANGE, // <-- Правильный атрибут для блоков
                        new AttributeModifier(BLOCK_REACH_ID, 0.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.ANY
                )
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE, // <-- Правильный атрибут для сущностей
                        new AttributeModifier(ENTITY_REACH_ID, 0.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.ANY
                )
                .build();
    }
}