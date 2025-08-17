package msu.msuteam.onlylaststand.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import msu.msuteam.onlylaststand.core.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class SpellScrollModifier extends LootModifier {
    public static final MapCodec<SpellScrollModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).apply(inst, SpellScrollModifier::new));

    public SpellScrollModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // ИСПРАВЛЕНО: Шанс изменен на 3%
        if (context.getRandom().nextFloat() < 0.03f) {
            generatedLoot.add(new ItemStack(ModItems.FIRE_SPELL_SCROLL.get()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends LootModifier> codec() {
        return CODEC;
    }
}