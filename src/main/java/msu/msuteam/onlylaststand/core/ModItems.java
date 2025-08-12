package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.item.accessories.SpeedRingItem; // <-- Импортируем новый класс
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OnlyLastStand.MODID);

    // ... материалы ...
    public static final DeferredItem<Item> UPGRADE_SHARD = ITEMS.registerSimpleItem("upgrade_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> UPGRADE_STONE = ITEMS.registerSimpleItem("upgrade_stone", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> SYNERGY_STONE = ITEMS.registerSimpleItem("synergy_stone", new Item.Properties().stacksTo(16));

    // Аксессуары
    public static final DeferredItem<Item> SPEED_RING = ITEMS.register("speed_ring", () -> new SpeedRingItem(new Item.Properties().stacksTo(1)));

    // Старый перстень можно удалить или закомментировать
    // public static final DeferredItem<Item> SIGNET_ACCESSORY = ITEMS.register("signet_accessory", () -> new AccessoryItem(new Item.Properties().stacksTo(1)));
}