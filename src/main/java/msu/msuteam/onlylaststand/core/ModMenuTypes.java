package msu.msuteam.onlylaststand.core;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.AccessoryMenu;
import msu.msuteam.onlylaststand.inventory.SpellMenu; // <-- ДОБАВЬТЕ ИМПОРТ
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import java.util.function.Supplier;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, OnlyLastStand.MODID);

    public static final Supplier<MenuType<AccessoryMenu>> ACCESSORY_MENU =
            MENUS.register("accessory_menu", () -> IMenuTypeExtension.create(AccessoryMenu::new));

    // --- ДОБАВЬТЕ ЭТОТ КОД ---
    public static final Supplier<MenuType<SpellMenu>> SPELL_MENU =
            MENUS.register("spell_menu", () -> IMenuTypeExtension.create(SpellMenu::new));
    // -------------------------
}