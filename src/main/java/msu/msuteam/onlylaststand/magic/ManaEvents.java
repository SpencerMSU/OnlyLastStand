package msu.msuteam.onlylaststand.magic;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class ManaEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            PlayerMana mana = player.getData(ModAttachments.PLAYER_MANA);

            float regenAmount = PlayerMana.DEFAULT_MANA_REGEN;

            // ИСПРАВЛЕНО: Теперь этот вызов корректен, так как MANA_REGENERATION имеет тип Holder
            if (player.hasEffect(ModEffects.MANA_REGENERATION)) {
                regenAmount += PlayerMana.DEFAULT_MANA_REGEN;
            }

            mana.regenerate(regenAmount);
        }
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getItem().is(Items.GOLDEN_APPLE) || event.getItem().is(Items.ENCHANTED_GOLDEN_APPLE)) {
                // ИСПРАВЛЕНО: И этот вызов теперь тоже корректен
                player.addEffect(new MobEffectInstance(ModEffects.MANA_REGENERATION, 2400, 0, false, true, true));
            }
        }
    }
}