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

        // --- Шаг 1: Удаляем ВСЕ старые модификаторы от аксессуаров ---
        // Мы ищем все модификаторы, ID которых начинается с "onlylaststand_", и удаляем их.
        // Это гарантирует полную очистку перед добавлением новых.
        player.getAttributes().getSyncableAttributes().forEach(attributeInstance -> {
            new ArrayList<>(attributeInstance.getModifiers()).forEach(modifier -> {
                if (modifier.id().getNamespace().equals(OnlyLastStand.MODID)) {
                    attributeInstance.removeModifier(modifier.id());
                }
            });
        });

        // --- Шаг 2: Применяем новые модификаторы для каждого слота индивидуально ---
        // Это ключевое изменение. Мы больше не суммируем баффы, а добавляем их по отдельности.
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AccessoryItem) {
                ItemAttributeModifiers itemModifiers = stack.getItem().getDefaultAttributeModifiers(stack);

                // Для каждого баффа от предмета...
                itemModifiers.modifiers().forEach(entry -> {
                    Holder<Attribute> attributeHolder = entry.attribute();
                    AttributeModifier modifier = entry.modifier();
                    AttributeInstance instance = player.getAttribute(attributeHolder);

                    if (instance != null) {
                        // Создаем УНИКАЛЬНЫЙ ID для каждого слота
                        // Например: "onlylaststand:speed_ring_bonus_slot_6"
                        ResourceLocation uniqueModifierId = modifier.id().withPath(p -> p + "_slot_" + i);

                        // Создаем новый модификатор с этим уникальным ID
                        AttributeModifier newModifier = new AttributeModifier(
                                uniqueModifierId,
                                modifier.amount(),
                                modifier.operation()
                        );

                        // Добавляем его как постоянный. Так как ID уникален, краша не будет.
                        instance.addPermanentModifier(newModifier);
                    }
                });
            }
        }
    }
}