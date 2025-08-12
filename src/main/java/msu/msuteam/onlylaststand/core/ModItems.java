package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.fire_collection.*;
import msu.msuteam.onlylaststand.item.accessories.water_collection.*; // Импорт водной коллекции
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OnlyLastStand.MODID);

    // Материалы
    public static final DeferredItem<Item> UPGRADE_SHARD = ITEMS.registerSimpleItem("upgrade_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> UPGRADE_STONE = ITEMS.registerSimpleItem("upgrade_stone", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> SYNERGY_STONE = ITEMS.registerSimpleItem("synergy_stone", new Item.Properties().stacksTo(16));

    // Огненная коллекция
    public static final DeferredItem<Item> FIRE_CROWN = ITEMS.register("fire_crown", () -> new CrownOfResilience(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_AMULET = ITEMS.register("fire_amulet", () -> new AmuletOfVitality(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_PAULDRON = ITEMS.register("fire_pauldron", () -> new PauldronOfFortitude(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_SPAULDER = ITEMS.register("fire_spaulder", () -> new SpaulderOfProtection(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GAUNTLETS = ITEMS.register("fire_gauntlets", () -> new GauntletsOfAlacrity(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_BAND = ITEMS.register("fire_band", () -> new BandOfReach(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_RING = ITEMS.register("fire_ring", () -> new SpeedRingItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_VAMBRACE = ITEMS.register("fire_vambrace", () -> new VambraceOfStrength(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GREAVES = ITEMS.register("fire_greaves", () -> new GreavesOfSafety(new Item.Properties().stacksTo(1)));

    // Водная коллекция
    public static final DeferredItem<Item> WATER_CROWN = ITEMS.register("water_crown", () -> new TidalCrown(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_AMULET = ITEMS.register("water_amulet", () -> new AmuletOfTheAbyss(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_PAULDRON = ITEMS.register("water_pauldron", () -> new CoralPauldron(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SPAULDER = ITEMS.register("water_spaulder", () -> new KelpSpaulder(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_GAUNTLETS = ITEMS.register("water_gauntlets", () -> new HarpoonersGrips(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_BAND = ITEMS.register("water_band", () -> new RingOfTides(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SIGNET = ITEMS.register("water_signet", () -> new PearlSignet(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_VAMBRACE = ITEMS.register("water_vambrace", () -> new FinBracers(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_GREAVES = ITEMS.register("water_greaves", () -> new AquaStriders(new Item.Properties().stacksTo(1)));
}