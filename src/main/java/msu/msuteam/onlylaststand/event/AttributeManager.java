package msu.msuteam.onlylaststand.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        // --- Шаг 1: Собираем все атрибуты и суммируем их значения по типам ---
        Map<Holder<Attribute>, Double> combinedValues = new HashMap<>();
        Map<Holder<Attribute>, AttributeModifier.Operation> operations = new HashMap<>();

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AccessoryItem) {
                ItemAttributeModifiers itemModifiers = stack.getItem().getDefaultAttributeModifiers(stack);
                itemModifiers.modifiers().forEach(entry -> {
                    Holder<Attribute> attribute = entry.attribute();
                    AttributeModifier modifier = entry.modifier();
                    combinedValues.merge(attribute, modifier.amount(), Double::sum);
                    operations.putIfAbsent(attribute, modifier.operation());
                });
            }
        }

        // --- Шаг 2: Удаляем ВСЕ наши старые модификаторы ---
        player.getAttributes().getSyncableAttributes().forEach(attributeInstance -> {
            new ArrayList<>(attributeInstance.getModifiers()).forEach(modifier -> {
                if (modifier.id().getNamespace().equals(OnlyLastStand.MODID)) {
                    attributeInstance.removeModifier(modifier.id());
                }
            });
        });

        // --- Шаг 3: Применяем новые, объединенные модификаторы как ПОСТОЯННЫЕ ---
        for (Map.Entry<Holder<Attribute>, Double> entry : combinedValues.entrySet()) {
            Holder<Attribute> attributeHolder = entry.getKey();
            double totalValue = entry.getValue();
            AttributeModifier.Operation operation = operations.get(attributeHolder);
            AttributeInstance instance = player.getAttribute(attributeHolder);

            if (instance != null && operation != null) {
                // ИСПРАВЛЕНО: Правильный способ получить ResourceLocation из Holder
                ResourceLocation attributeId = attributeHolder.unwrapKey().orElseThrow().location();
                ResourceLocation modifierId = attributeId.withPrefix("onlylaststand_");

                AttributeModifier newModifier = new AttributeModifier(
                        modifierId,
                        totalValue,
                        operation
                );
                instance.addPermanentModifier(newModifier);
            }
        }
    }
}