package msu.msuteam.onlylaststand.util;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> SPELLS = create("spells");
        public static final TagKey<Item> FIRE_SPELLS = create("fire_spells");
        public static final TagKey<Item> WATER_SPELLS = create("water_spells"); // ДОБАВЛЕНО

        private static TagKey<Item> create(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, name));
        }
    }
}