package msu.msuteam.onlylaststand;
import msu.msuteam.onlylaststand.magic.ModEffects;
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

                output.accept(ModItems.UPGRADE_SHARD.get());
                output.accept(ModItems.UPGRADE_STONE.get());
                output.accept(ModItems.SYNERGY_STONE.get());
                output.accept(ModItems.FIRE_CROWN.get());
                output.accept(ModItems.FIRE_AMULET.get());
                output.accept(ModItems.FIRE_PAULDRON.get());
                output.accept(ModItems.FIRE_SPAULDER.get());
                output.accept(ModItems.FIRE_GAUNTLETS.get());
                output.accept(ModItems.FIRE_BAND.get());
                output.accept(ModItems.FIRE_RING.get());
                output.accept(ModItems.FIRE_VAMBRACE.get());
                output.accept(ModItems.FIRE_GREAVES.get());
                output.accept(ModItems.WATER_CROWN.get());
                output.accept(ModItems.WATER_AMULET.get());
                output.accept(ModItems.WATER_PAULDRON.get());
                output.accept(ModItems.WATER_SPAULDER.get());
                output.accept(ModItems.WATER_GAUNTLETS.get());
                output.accept(ModItems.WATER_BAND.get());
                output.accept(ModItems.WATER_SIGNET.get());
                output.accept(ModItems.WATER_VAMBRACE.get());
                output.accept(ModItems.WATER_GREAVES.get());

            }).build());

    public OnlyLastStand(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModDataComponents.COMPONENT_TYPES.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(PacketHandler::register);
        ModEffects.register(modEventBus);
    }
}