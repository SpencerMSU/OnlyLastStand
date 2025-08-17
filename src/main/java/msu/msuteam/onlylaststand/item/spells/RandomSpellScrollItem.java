package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.network.SyncLearnedSpellsPacket;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomSpellScrollItem extends Item {

    public RandomSpellScrollItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            PlayerLearnedSpells learnedSpells = pPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);

            Optional<List<Holder<Item>>> allFireSpellsOpt = BuiltInRegistries.ITEM.getTag(ModTags.Items.FIRE_SPELLS).map(tag -> tag.stream().toList());
            if (allFireSpellsOpt.isEmpty()) {
                return InteractionResultHolder.fail(heldStack);
            }

            List<Item> unlearnedSpells = new ArrayList<>();
            for (Holder<Item> spellHolder : allFireSpellsOpt.get()) {
                if (!learnedSpells.hasLearned(spellHolder.value())) {
                    unlearnedSpells.add(spellHolder.value());
                }
            }

            if (unlearnedSpells.isEmpty()) {
                pPlayer.sendSystemMessage(Component.literal("Вы уже изучили все огненные заклинания!"));
                return InteractionResultHolder.fail(heldStack);
            }

            Item spellToLearn = unlearnedSpells.get(pLevel.random.nextInt(unlearnedSpells.size()));
            learnedSpells.learnSpell(spellToLearn);

            // Отправляем пакет на клиент для обновления интерфейса
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SyncLearnedSpellsPacket(learnedSpells.serializeNBT(serverPlayer.registryAccess())));
            }

            pPlayer.sendSystemMessage(Component.literal("Вы изучили новое заклинание: " + spellToLearn.getDescription().getString()));
            pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.0f);

            if (!pPlayer.isCreative()) {
                heldStack.shrink(1);
            }

            return InteractionResultHolder.success(heldStack);
        }

        return InteractionResultHolder.pass(heldStack);
    }
}