package msu.msuteam.onlylaststand.component;

import com.mojang.serialization.Codec;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.UnaryOperator;
import net.minecraft.core.registries.Registries;
import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, OnlyLastStand.MODID);

    public static final Supplier<DataComponentType<Integer>> ACCESSORY_LEVEL = register("accessory_level",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final Supplier<DataComponentType<Rarity>> ACCESSORY_RARITY = register("accessory_rarity",
            builder -> builder.persistent(Codec.STRING.xmap(Rarity::valueOf, Rarity::name))
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8.map(Rarity::valueOf, Rarity::name)));

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return COMPONENT_TYPES.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }
}