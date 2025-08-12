package msu.msuteam.onlylaststand.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class AccessoryDropModifier extends LootModifier {
    public static final MapCodec<AccessoryDropModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, AccessoryDropModifier::new));

    private static final Random random = new Random();

    private static final List<DeferredItem<Item>> ACCESSORY_POOL = List.of(
            ModItems.HAT_ACCESSORY, ModItems.NECKLACE_ACCESSORY, ModItems.RIGHT_SHOULDER_ACCESSORY,
            ModItems.LEFT_SHOULDER_ACCESSORY, ModItems.GLOVES_ACCESSORY, ModItems.RING_SET_ACCESSORY,
            ModItems.SPEED_RING, ModItems.ELBOW_PAD_ACCESSORY, ModItems.KNEE_PAD_ACCESSORY
    );

    public AccessoryDropModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() <= 0.05f) {
            Rarity rarity = getWeightedRarity();
            Item accessoryItem = ACCESSORY_POOL.get(random.nextInt(ACCESSORY_POOL.size())).get();
            ItemStack stack = new ItemStack(accessoryItem);
            stack.set(ModDataComponents.ACCESSORY_RARITY, rarity);
            generatedLoot.add(stack);
        }
        return generatedLoot;
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
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}