package msu.msuteam.onlylaststand.event;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.AccessoryInventory;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class AttributeManager {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide || player.tickCount % 20 != 0) {
            return;
        }

        AccessoryInventory inventory = player.getData(ModAttachments.ACCESSORY_INVENTORY);
        if (inventory == null) return;

        player.getAttributes().getSyncableAttributes().forEach(attributeInstance -> {
            new ArrayList<>(attributeInstance.getModifiers()).forEach(modifier -> {
                if (modifier.id().getNamespace().equals(OnlyLastStand.MODID)) {
                    attributeInstance.removeModifier(modifier.id());
                }
            });
        });

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AccessoryItem accessory) {
                // ИСПРАВЛЕНО: Вызываем наш новый кастомный метод
                ItemAttributeModifiers itemModifiers = accessory.getAccessoryAttributeModifiers(stack);

                final int slotIndex = i; // ИСПРАВЛЕНО: Создаем final-переменную для лямбды
                itemModifiers.modifiers().forEach(entry -> {
                    Holder<Attribute> attributeHolder = entry.attribute();
                    AttributeModifier modifier = entry.modifier();
                    AttributeInstance instance = player.getAttribute(attributeHolder);

                    if (instance != null) {
                        ResourceLocation uniqueModifierId = modifier.id().withPath(p -> p + "_slot_" + slotIndex);
                        AttributeModifier newModifier = new AttributeModifier(
                                uniqueModifierId,
                                modifier.amount(),
                                modifier.operation()
                        );
                        instance.addPermanentModifier(newModifier);
                    }
                });
            }
        }
    }
}