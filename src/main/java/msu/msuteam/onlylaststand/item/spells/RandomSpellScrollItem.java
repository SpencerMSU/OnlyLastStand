package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.tags.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class RandomSpellScrollItem extends Item {

    private final List<TagKey<Item>> allowedTags;

    public RandomSpellScrollItem(Properties pProperties) {
        super(pProperties);
        // по умолчанию — старая логика: FIRE, затем WATER
        this.allowedTags = List.of(ModTags.Items.FIRE_SPELLS, ModTags.Items.WATER_SPELLS);
    }

    public RandomSpellScrollItem(Properties pProperties, List<TagKey<Item>> allowedTags) {
        super(pProperties);
        this.allowedTags = allowedTags;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Попытка открыть следующее заклинание в очереди."));
        tooltip.add(Component.literal("Шанс успеха уменьшается по мере прогресса:"));
        tooltip.add(Component.literal("≈ 50%, 45%, 40%, ... минимум 10%."));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            PlayerLearnedSpells learned = pPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);

            // Формируем очередь ТОЛЬКО из разрешённых тегов
            List<Item> progression = new ArrayList<>();
            for (TagKey<Item> tag : allowedTags) {
                appendTagItemsInRegistryOrder(progression, tag);
            }

            // Находим первое неоткрытое
            int nextIndex = -1;
            Item nextSpell = null;
            for (int i = 0; i < progression.size(); i++) {
                Item it = progression.get(i);
                if (!learned.hasLearned(it)) {
                    nextIndex = i;
                    nextSpell = it;
                    break;
                }
            }

            if (nextSpell == null) {
                pPlayer.sendSystemMessage(Component.literal("Все заклинания этой школы уже открыты."));
                return InteractionResultHolder.fail(heldStack);
            }

            double chance = Math.max(0.10, 0.50 - (nextIndex * 0.05));
            boolean success = pLevel.random.nextDouble() < chance;

            if (success) {
                learned.learnSpell(nextSpell);
                if (!pPlayer.isCreative()) {
                    heldStack.shrink(1);
                }
                pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.0f);
                pPlayer.sendSystemMessage(Component.literal("Вы освоили новое заклинание!"));
            } else {
                if (!pPlayer.isCreative()) {
                    heldStack.shrink(1);
                }
                pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.7f, 0.9f);
                pPlayer.sendSystemMessage(Component.literal("Не удалось раскрыть следующее заклинание..."));
            }

            // Если у тебя есть пакет синхронизации — оставляем как есть, иначе можно убрать
            // PacketDistributor.sendToPlayer(...)
            return InteractionResultHolder.success(heldStack);
        }

        return InteractionResultHolder.pass(heldStack);
    }

    private void appendTagItemsInRegistryOrder(List<Item> dst, TagKey<Item> tag) {
        BuiltInRegistries.ITEM.getTag(tag).ifPresent(holderSet -> holderSet.stream()
                .map(h -> h.value())
                .forEachOrdered(dst::add));
    }
}