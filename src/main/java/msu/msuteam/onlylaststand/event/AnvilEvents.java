package msu.msuteam.onlylaststand.event;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class AnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        // --- Логика Улучшения Уровня ---
        if (left.getItem() instanceof AccessoryItem && right.is(ModItems.UPGRADE_STONE.get())) {
            int currentLevel = left.getOrDefault(ModDataComponents.ACCESSORY_LEVEL, 0);
            if (currentLevel >= 10) return; // Максимальный уровень

            int stonesNeeded = currentLevel + 1;
            if (right.getCount() < stonesNeeded) return; // Недостаточно камней

            out = left.copy();
            out.set(ModDataComponents.ACCESSORY_LEVEL, currentLevel + 1);

            event.setCost(stonesNeeded * 2); // Стоимость в уровнях опыта
            event.setMaterialCost(stonesNeeded); // Сколько камней потратить
            event.setOutput(out);
        }
        // --- Логика Повышения Редкости ---
        else if (left.getItem() instanceof AccessoryItem && right.is(ModItems.SYNERGY_STONE.get())) {
            Rarity currentRarity = left.getOrDefault(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON);
            Rarity nextRarity = getNextRarity(currentRarity);

            if (nextRarity == null) return; // Уже максимальная редкость

            // Здесь должна быть логика шансов, но для простоты пока делаем 100%
            // TODO: Добавить отображение шанса и саму логику при взятии предмета

            out = left.copy();
            out.set(ModDataComponents.ACCESSORY_RARITY, nextRarity);
            event.setCost(10); // Примерная стоимость в опыте
            event.setMaterialCost(1);
            event.setOutput(out);
        }
    }

    private static Rarity getNextRarity(Rarity current) {
        return switch (current) {
            case COMMON -> Rarity.RARE;
            case RARE -> Rarity.EPIC;
            case EPIC -> Rarity.LEGENDARY;
            case LEGENDARY -> Rarity.MYTHIC;
            case MYTHIC -> Rarity.ABSOLUTE;
            default -> null; // Уже максимальная
        };
    }
}