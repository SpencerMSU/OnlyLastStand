package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.util.ModTags; // <-- ИСПРАВЛЕНО: Добавлен импорт
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RandomSpellScrollItem extends Item {

    public RandomSpellScrollItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            PlayerLearnedSpells learnedSpells = pPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);

            List<Holder<Item>> allFireSpells = BuiltInRegistries.ITEM.getTag(ModTags.Items.FIRE_SPELLS).map(tag -> tag.stream().toList()).orElse(List.of());
            List<Item> unlearnedSpells = new ArrayList<>();

            for (Holder<Item> spellHolder : allFireSpells) {
                if (!learnedSpells.hasLearned(spellHolder.value())) {
                    unlearnedSpells.add(spellHolder.value());
                }
            }

            if (unlearnedSpells.isEmpty()) {
                pPlayer.sendSystemMessage(Component.literal("Вы уже изучили все огненные заклинания!"));
                return InteractionResultHolder.fail(heldStack);
            }

            Item spellToLearn = unlearnedSpells.get(pLevel.random.nextInt(unlearnedSpells.size()));
            ItemStack spellItemStack = new ItemStack(spellToLearn);

            learnedSpells.learnSpell(spellToLearn);

            pPlayer.sendSystemMessage(Component.literal("Вы изучили новое заклинание: " + spellItemStack.getHoverName().getString()));
            pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.0f);

            if (!pPlayer.isCreative()) {
                heldStack.shrink(1);
            }

            if (!pPlayer.getInventory().add(spellItemStack)) {
                pPlayer.drop(spellItemStack, false);
            }

            return InteractionResultHolder.success(heldStack);
        }

        return InteractionResultHolder.pass(heldStack);
    }
}