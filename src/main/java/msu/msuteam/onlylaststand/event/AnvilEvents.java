package msu.msuteam.onlylaststand.event;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

import java.util.Random;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class AnvilEvents {

    private static final Random random = new Random();

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.getItem() instanceof AccessoryItem && right.is(ModItems.UPGRADE_STONE.get())) {
            int currentLevel = left.getOrDefault(ModDataComponents.ACCESSORY_LEVEL, 0);
            if (currentLevel >= 10) return;

            int stonesNeeded = currentLevel + 1;
            if (right.getCount() < stonesNeeded) return;

            out = left.copy();

            PlayerSkills skills = event.getPlayer().getData(ModAttachments.PLAYER_SKILLS);
            int smithingLevel = skills.getSkill(PlayerSkill.SMITHING).getLevel();
            double smithingBonus = smithingLevel * 0.0005;
            double successChance = Math.max(0.01, 1.0 / Math.pow(2, currentLevel)) + smithingBonus;

            String chanceText = String.format("%.2f%%", successChance * 100);
            out.set(DataComponents.CUSTOM_NAME, Component.literal("Улучшить? Шанс: " + chanceText).withStyle(ChatFormatting.YELLOW));

            float discount = 1.0f - (smithingLevel * 0.0045f);
            int finalCost = Math.max(1, (int)((stonesNeeded * 2) * discount));

            event.setCost(finalCost);
            event.setMaterialCost(stonesNeeded);
            event.setOutput(out);
        }
        else if (left.getItem() instanceof AccessoryItem && right.is(ModItems.SYNERGY_STONE.get())) {
            Rarity currentRarity = left.getOrDefault(ModDataComponents.ACCESSORY_RARITY, Rarity.COMMON);
            Rarity nextRarity = getNextRarity(currentRarity);

            if (nextRarity == null) return;

            out = left.copy();
            out.set(ModDataComponents.ACCESSORY_RARITY, nextRarity);
            event.setCost(10);
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
            default -> null;
        };
    }
}