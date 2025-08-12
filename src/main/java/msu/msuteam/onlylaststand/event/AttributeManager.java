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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList; // ИСПРАВЛЕНО: Добавлен импорт
import java.util.List;      // ИСПРАВЛЕНО: Добавлен импорт

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

        // --- Шаг 1: Удаляем ВСЕ старые модификаторы, которые были добавлены нами ---
        player.getAttributes().getSyncableAttributes().forEach(attributeInstance -> {
            new ArrayList<>(attributeInstance.getModifiers()).forEach(modifier -> {
                if (modifier.id().getNamespace().equals(OnlyLastStand.MODID)) {
                    attributeInstance.removeModifier(modifier.id());
                }
            });
        });

        // --- Шаг 2: Собираем все новые атрибуты ---
        Multimap<Holder<Attribute>, AttributeModifier> newModifiers = HashMultimap.create();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AccessoryItem) {
                ItemAttributeModifiers itemModifiers = stack.getItem().getDefaultAttributeModifiers(stack);
                // ИСПРАВЛЕНО: Правильно перебираем List и добавляем в Multimap
                itemModifiers.modifiers().forEach(entry -> newModifiers.put(entry.attribute(), entry.modifier()));
            }
        }

        // --- Шаг 3: Применяем все собранные модификаторы ---
        if (!newModifiers.isEmpty()) {
            player.getAttributes().addTransientAttributeModifiers(newModifiers);
        }
    }
}