package msu.msuteam.onlylaststand.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.util.CollectionType;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class AccessoryDropModifier extends LootModifier {
    public static final MapCodec<AccessoryDropModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, AccessoryDropModifier::new));

    private static final Random random = new Random();

    private static final List<DeferredItem<Item>> FIRE_COLLECTION_POOL = List.of(
            ModItems.FIRE_CROWN, ModItems.FIRE_AMULET, ModItems.FIRE_PAULDRON, ModItems.FIRE_SPAULDER,
            ModItems.FIRE_GAUNTLETS, ModItems.FIRE_BAND, ModItems.FIRE_RING, ModItems.FIRE_VAMBRACE, ModItems.FIRE_GREAVES
    );

    private static final List<DeferredItem<Item>> WATER_COLLECTION_POOL = List.of(
            ModItems.WATER_CROWN, ModItems.WATER_AMULET, ModItems.WATER_PAULDRON, ModItems.WATER_SPAULDER,
            ModItems.WATER_GAUNTLETS, ModItems.WATER_BAND, ModItems.WATER_SIGNET, ModItems.WATER_VAMBRACE, ModItems.WATER_GREAVES
    );

    public AccessoryDropModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() > 0.05f) {
            return generatedLoot;
        }

        CollectionType chosenCollection = rollCollection();
        List<DeferredItem<Item>> chosenPool = getPoolForCollection(chosenCollection);

        if (chosenPool == null || chosenPool.isEmpty()) {
            return generatedLoot;
        }

        Item accessoryItem = chosenPool.get(random.nextInt(chosenPool.size())).get();
        ItemStack stack = new ItemStack(accessoryItem);

        Rarity rarity = getWeightedRarity();
        stack.set(ModDataComponents.ACCESSORY_RARITY, rarity);

        generatedLoot.add(stack);
        return generatedLoot;
    }

    private CollectionType rollCollection() {
        int totalWeight = 0;
        for (CollectionType type : CollectionType.values()) {
            if (type != CollectionType.NONE) {
                totalWeight += type.getWeight();
            }
        }
        if (totalWeight <= 0) return CollectionType.NONE;

        int roll = random.nextInt(totalWeight);
        int currentWeight = 0;
        for (CollectionType type : CollectionType.values()) {
            if (type != CollectionType.NONE) {
                currentWeight += type.getWeight();
                if (roll < currentWeight) {
                    return type;
                }
            }
        }
        return CollectionType.NONE;
    }

    private List<DeferredItem<Item>> getPoolForCollection(CollectionType collection) {
        return switch (collection) {
            case FIRE -> FIRE_COLLECTION_POOL;
            case WATER -> WATER_COLLECTION_POOL;
            default -> null;
        };
    }

    private Rarity getWeightedRarity() {
        int chance = random.nextInt(100);
        if (chance < 2) return Rarity.MYTHIC;
        if (chance < 10) return Rarity.LEGENDARY;
        if (chance < 20) return Rarity.EPIC;
        if (chance < 40) return Rarity.RARE;
        return Rarity.COMMON;
    }

    @Override
    public MapCodec<? extends LootModifier> codec() {
        return CODEC;
    }
}