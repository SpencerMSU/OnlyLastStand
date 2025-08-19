package msu.msuteam.onlylaststand.event;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class PlayerCloneHandler {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        // Сохраняем выученные заклинания
        PlayerLearnedSpells oldLearned = oldPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
        PlayerLearnedSpells newLearned = newPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
        newLearned.deserializeNBT(newPlayer.registryAccess(), oldLearned.serializeNBT(newPlayer.registryAccess()));

        // Сохраняем инвентарь заклинаний (ячейки с предметами-заклинаниями)
        SpellInventory oldInv = oldPlayer.getData(ModAttachments.SPELL_INVENTORY);
        SpellInventory newInv = newPlayer.getData(ModAttachments.SPELL_INVENTORY);
        newInv.deserializeNBT(newPlayer.registryAccess(), oldInv.serializeNBT(newPlayer.registryAccess()));
    }
}