package msu.msuteam.onlylaststand.event;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerSpellCooldowns;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class CooldownRestoreHandler {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        reapplyCooldowns(e.getEntity());
    }

    @SubscribeEvent
    public static void onChangedDim(PlayerEvent.PlayerChangedDimensionEvent e) {
        reapplyCooldowns(e.getEntity());
    }

    private static void reapplyCooldowns(Player player) {
        if (player == null || player.level().isClientSide()) return;
        PlayerSpellCooldowns store = player.getData(ModAttachments.PLAYER_SPELL_COOLDOWNS);

        // Реапплай КД только для предметов из тега spells
        BuiltInRegistries.ITEM.getTag(ModTags.Items.SPELLS).ifPresent(set -> set.forEach(h -> {
            Item item = h.value();
            int remain = store.getRemainingTicks(player.level(), item);
            if (remain > 0) {
                player.getCooldowns().addCooldown(item, remain);
            }
        }));
    }
}