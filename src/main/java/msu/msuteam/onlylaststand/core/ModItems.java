package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.item.accessories.elbow.VambraceOfStrength;
import msu.msuteam.onlylaststand.item.accessories.gloves.GauntletsOfAlacrity;
import msu.msuteam.onlylaststand.item.accessories.head.CrownOfResilience;
import msu.msuteam.onlylaststand.item.accessories.knee.GreavesOfSafety;
import msu.msuteam.onlylaststand.item.accessories.neck.AmuletOfVitality;
import msu.msuteam.onlylaststand.item.accessories.rings.BandOfReach;
import msu.msuteam.onlylaststand.item.accessories.rings.SpeedRingItem;
import msu.msuteam.onlylaststand.item.accessories.shoulder.PauldronOfFortitude;
import msu.msuteam.onlylaststand.item.accessories.shoulder.SpaulderOfProtection;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OnlyLastStand.MODID);

    // Материалы
    public static final DeferredItem<Item> UPGRADE_SHARD = ITEMS.registerSimpleItem("upgrade_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> UPGRADE_STONE = ITEMS.registerSimpleItem("upgrade_stone", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> SYNERGY_STONE = ITEMS.registerSimpleItem("synergy_stone", new Item.Properties().stacksTo(16));

    // Аксессуары
    public static final DeferredItem<Item> CROWN_OF_RESILIENCE = ITEMS.register("crown_of_resilience", () -> new CrownOfResilience(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> AMULET_OF_VITALITY = ITEMS.register("amulet_of_vitality", () -> new AmuletOfVitality(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PAULDRON_OF_FORTITUDE = ITEMS.register("pauldron_of_fortitude", () -> new PauldronOfFortitude(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SPAULDER_OF_PROTECTION = ITEMS.register("spaulder_of_protection", () -> new SpaulderOfProtection(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GAUNTLETS_OF_ALACRITY = ITEMS.register("gauntlets_of_alacrity", () -> new GauntletsOfAlacrity(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BAND_OF_REACH = ITEMS.register("band_of_reach", () -> new BandOfReach(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SPEED_RING = ITEMS.register("speed_ring", () -> new SpeedRingItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> VAMBRACE_OF_STRENGTH = ITEMS.register("vambrace_of_strength", () -> new VambraceOfStrength(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GREAVES_OF_SAFETY = ITEMS.register("greaves_of_safety", () -> new GreavesOfSafety(new Item.Properties().stacksTo(1)));
}