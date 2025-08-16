package msu.msuteam.onlylaststand.core;

import com.mojang.serialization.MapCodec;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.loot.AccessoryDropModifier;
import msu.msuteam.onlylaststand.loot.ShardDropModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;
import msu.msuteam.onlylaststand.loot.SpellScrollModifier;
public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, OnlyLastStand.MODID);

    public static final Supplier<MapCodec<AccessoryDropModifier>> ADD_ACCESSORY =
            LOOT_MODIFIER_SERIALIZERS.register("add_accessory", () -> AccessoryDropModifier.CODEC);

    public static final Supplier<MapCodec<ShardDropModifier>> ADD_SHARD =
            LOOT_MODIFIER_SERIALIZERS.register("add_shard", () -> ShardDropModifier.CODEC);
    public static final Supplier<MapCodec<SpellScrollModifier>> ADD_SPELL_SCROLL =
            LOOT_MODIFIER_SERIALIZERS.register("add_spell_scroll", () -> SpellScrollModifier.CODEC);

}