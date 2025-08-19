package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.accessories.fire_collection.*;
import msu.msuteam.onlylaststand.item.accessories.water_collection.*;
import msu.msuteam.onlylaststand.item.spells.fire_school.*;
import msu.msuteam.onlylaststand.item.spells.water_school.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OnlyLastStand.MODID);

    // Материалы
    public static final DeferredItem<Item> UPGRADE_SHARD = ITEMS.registerSimpleItem("upgrade_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> UPGRADE_STONE = ITEMS.registerSimpleItem("upgrade_stone", new Item.Properties().stacksTo(16));
    public static final DeferredItem<Item> SYNERGY_STONE = ITEMS.registerSimpleItem("synergy_stone", new Item.Properties().stacksTo(16));

    // Скроллы (плейсхолдер для совместимости с ModEvents/loot)
    public static final DeferredItem<Item> FIRE_SPELL_SCROLL = ITEMS.registerSimpleItem("fire_spell_scroll", new Item.Properties().stacksTo(64));

    // ОГНЕННЫЕ аксессуары (как были)
    public static final DeferredItem<Item> FIRE_CROWN = ITEMS.register("fire_crown", () -> new CrownOfResilience(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_AMULET = ITEMS.register("fire_amulet", () -> new AmuletOfVitality(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_PAULDRON = ITEMS.register("fire_pauldron", () -> new PauldronOfFortitude(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_SPAULDER = ITEMS.register("fire_spaulder", () -> new SpaulderOfProtection(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GAUNTLETS = ITEMS.register("fire_gauntlets", () -> new GauntletsOfAlacrity(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_BAND = ITEMS.register("fire_band", () -> new BandOfReach(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_RING = ITEMS.register("fire_ring", () -> new SpeedRingItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_VAMBRACE = ITEMS.register("fire_vambrace", () -> new VambraceOfStrength(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_GREAVES = ITEMS.register("fire_greaves", () -> new GreavesOfSafety(new Item.Properties().stacksTo(1)));

    // ВОДНЫЕ аксессуары (из имеющихся классов + плейсхолдеры для отсутствующих)
    public static final DeferredItem<Item> WATER_CROWN = ITEMS.register("water_crown", () -> new TidalCrown(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_AMULET = ITEMS.register("water_amulet", () -> new AmuletOfTheAbyss(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_PAULDRON = ITEMS.register("water_pauldron", () -> new CoralPauldron(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SPAULDER = ITEMS.register("water_spaulder", () -> new KelpSpaulder(new Item.Properties().stacksTo(1)));
    // Эти пять ожидались другим кодом — если классов нет, используем плейсхолдеры, чтобы сборка проходила
    public static final DeferredItem<Item> WATER_GAUNTLETS = ITEMS.registerSimpleItem("water_gauntlets", new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> WATER_BAND = ITEMS.registerSimpleItem("water_band", new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> WATER_SIGNET = ITEMS.register("water_signet", () -> new PearlSignet(new Item.Properties().stacksTo(1))); // есть класс
    public static final DeferredItem<Item> WATER_VAMBRACE = ITEMS.registerSimpleItem("water_vambrace", new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> WATER_GREAVES = ITEMS.registerSimpleItem("water_greaves", new Item.Properties().stacksTo(1));

    // ОГНЕННЫЕ заклинания (для совместимости с OnlyLastStand)
    public static final DeferredItem<Item> FIRE_PROJECTILE_SPELL = ITEMS.register("fire_projectile_spell", () -> new FireProjectileSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DRAGONS_BREATH_SPELL = ITEMS.registerSimpleItem("dragons_breath_spell", new Item.Properties().stacksTo(1)); // плейсхолдер, если класса нет
    public static final DeferredItem<Item> MAGMA_BLOOD_SPELL = ITEMS.register("magma_blood_spell", () -> new MagmaBloodSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_SHIELD_SPELL = ITEMS.register("fire_shield_spell", () -> new FireShieldSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FIRE_PILLAR_SPELL = ITEMS.register("fire_pillar_spell", () -> new FirePillarSpell(new Item.Properties().stacksTo(1)));
    // Доп. огненные (если используешь их где-то)
    public static final DeferredItem<Item> BLADES_OF_FIRE_SPELL = ITEMS.register("blades_of_fire_spell", () -> new BladesOfFireSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> HELP_FROM_HELL_SPELL = ITEMS.register("help_from_hell_spell", () -> new HelpFromHellSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SATANS_HELP_SPELL = ITEMS.register("satans_help_spell", () -> new SatansHelpSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> COSMIC_METEOR_SPELL = ITEMS.register("cosmic_meteor_spell", () -> new CosmicMeteorSpell(new Item.Properties().stacksTo(1)));

    // ВОДНАЯ школа — 10 заклинаний, 6 редкостей
    public static final DeferredItem<Item> WATER_RIPPLE_DART = ITEMS.register("water_ripple_dart", () -> new RippleDartSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_TIDAL_STEP = ITEMS.register("water_tidal_step", () -> new TidalStepSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_BUBBLE_PRISON = ITEMS.register("water_bubble_prison", () -> new BubblePrisonSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_HEALING_RAIN = ITEMS.register("water_healing_rain", () -> new HealingRainSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_FROST_TRIDENT = ITEMS.register("water_frost_trident", () -> new FrostTridentSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_MAELSTROM = ITEMS.register("water_maelstrom", () -> new MaelstromSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_MIRROR_OF_TIDES = ITEMS.register("water_mirror_of_tides", () -> new MirrorOfTidesSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_LEVIATHANS_CHARGE = ITEMS.register("water_leviathans_charge", () -> new LeviathansChargeSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_TSUNAMI = ITEMS.register("water_tsunami", () -> new TsunamiSpell(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WATER_SIRENS_ARIA = ITEMS.register("water_sirens_aria", () -> new SirensAriaSpell(new Item.Properties().stacksTo(1)));
}