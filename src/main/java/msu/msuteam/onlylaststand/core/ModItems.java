package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.fire_collection.*;
import msu.msuteam.onlylaststand.item.accessories.water_collection.*;
import msu.msuteam.onlylaststand.item.spells.RandomSpellScrollItem;
import msu.msuteam.onlylaststand.item.spells.fire_school.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OnlyLastStand.MODID);

    public static final DeferredItem<Item> UPGRADE_SHARD = ITEMS.registerSimpleItem("upgrade_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> UPGRADE_STONE = ITEMS.registerSimpleItem("upgrade_stone", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> SYNERGY_STONE = ITEMS.registerSimpleItem("synergy_stone", new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> FIRE_CROWN = ITEMS.register("fire_crown", () -> new CrownOfResilience(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_AMULET = ITEMS.register("fire_amulet", () -> new AmuletOfVitality(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_PAULDRON = ITEMS.register("fire_pauldron", () -> new PauldronOfFortitude(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_SPAULDER = ITEMS.register("fire_spaulder", () -> new SpaulderOfProtection(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GAUNTLETS = ITEMS.register("fire_gauntlets", () -> new GauntletsOfAlacrity(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_BAND = ITEMS.register("fire_band", () -> new BandOfReach(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_RING = ITEMS.register("fire_ring", () -> new SpeedRingItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_VAMBRACE = ITEMS.register("fire_vambrace", () -> new VambraceOfStrength(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GREAVES = ITEMS.register("fire_greaves", () -> new GreavesOfSafety(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> WATER_CROWN = ITEMS.register("water_crown", () -> new TidalCrown(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_AMULET = ITEMS.register("water_amulet", () -> new AmuletOfTheAbyss(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_PAULDRON = ITEMS.register("water_pauldron", () -> new CoralPauldron(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SPAULDER = ITEMS.register("water_spaulder", () -> new KelpSpaulder(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_GAUNTLETS = ITEMS.register("water_gauntlets", () -> new HarpoonersGrips(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_BAND = ITEMS.register("water_band", () -> new RingOfTides(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SIGNET = ITEMS.register("water_signet", () -> new PearlSignet(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_VAMBRACE = ITEMS.register("water_vambrace", () -> new FinBracers(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_GREAVES = ITEMS.register("water_greaves", () -> new AquaStriders(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FIRE_PROJECTILE_SPELL = ITEMS.register("fire_projectile_spell",
            () -> new FireProjectileSpell(new Item.Properties()));
    public static final DeferredItem<Item> DRAGONS_BREATH_SPELL = ITEMS.register("dragons_breath_spell",
            () -> new DragonsBreathSpell(new Item.Properties()));
    public static final DeferredItem<Item> MAGMA_BLOOD_SPELL = ITEMS.register("magma_blood_spell",
            () -> new MagmaBloodSpell(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_SHIELD_SPELL = ITEMS.register("fire_shield_spell",
            () -> new FireShieldSpell(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_PILLAR_SPELL = ITEMS.register("fire_pillar_spell",
            () -> new FirePillarSpell(new Item.Properties()));
    public static final DeferredItem<Item> BLADES_OF_FIRE_SPELL = ITEMS.register("blades_of_fire_spell",
            () -> new BladesOfFireSpell(new Item.Properties()));
    public static final DeferredItem<Item> DEVILS_TOOLS_SPELL = ITEMS.register("devils_tools_spell",
            () -> new DevilsToolsSpell(new Item.Properties()));
    public static final DeferredItem<Item> HELP_FROM_HELL_SPELL = ITEMS.register("help_from_hell_spell",
            () -> new HelpFromHellSpell(new Item.Properties()));
    public static final DeferredItem<Item> COSMIC_METEOR_SPELL = ITEMS.register("cosmic_meteor_spell",
            () -> new CosmicMeteorSpell(new Item.Properties()));
    public static final DeferredItem<Item> SATANS_HELP_SPELL = ITEMS.register("satans_help_spell",
            () -> new SatansHelpSpell(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_SPELL_SCROLL = ITEMS.register("fire_spell_scroll",
            () -> new RandomSpellScrollItem(new Item.Properties()));
}