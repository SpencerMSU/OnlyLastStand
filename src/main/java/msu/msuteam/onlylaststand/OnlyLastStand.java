package msu.msuteam.onlylaststand;

import com.mojang.logging.LogUtils;
import msu.msuteam.onlylaststand.component.ModDataComponents;
import msu.msuteam.onlylaststand.core.ModItems;
import msu.msuteam.onlylaststand.core.ModLootModifiers;
import msu.msuteam.onlylaststand.core.ModMenuTypes;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.network.PacketHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(OnlyLastStand.MODID)
public class OnlyLastStand {
    public static final String MODID = "onlylaststand";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OLS_TAB = CREATIVE_MODE_TABS.register("ols_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.onlylaststand"))
            .icon(() -> ModItems.SYNERGY_STONE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                // --- Добавляем только нужные предметы ---

                // Материалы
                output.accept(ModItems.UPGRADE_SHARD.get());
                output.accept(ModItems.UPGRADE_STONE.get());
                output.accept(ModItems.SYNERGY_STONE.get());

                // Уникальные аксессуары
                output.accept(ModItems.SPEED_RING.get());

            }).build());

    public OnlyLastStand(IEventBus modEventBus, ModContainer modContainer) {
        // --- РЕГИСТРАЦИЯ ВСЕХ КОМПОНЕНТОВ МОДА ---
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModDataComponents.COMPONENT_TYPES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);

        // Регистрируем обработчик сетевых пакетов
        modEventBus.addListener(PacketHandler::register);

        // NeoForge.EVENT_BUS.register(this); // ИСПРАВЛЕНО: Эта строка УДАЛЕНА
    }
}