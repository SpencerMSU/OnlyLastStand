package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.network.SyncLearnedSpellsPacket;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomSpellScrollItem extends Item {

    public RandomSpellScrollItem(Properties pProperties) {
        super(pProperties);
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

            // Формируем общую упорядоченную очередь: сначала огненная школа, затем водная
            List<Item> progression = new ArrayList<>();
            appendTagItemsInRegistryOrder(progression, ModTags.Items.FIRE_SPELLS);
            appendTagItemsInRegistryOrder(progression, ModTags.Items.WATER_SPELLS);

            // Находим первое неоткрытое — это «следующее в очереди»
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
                pPlayer.sendSystemMessage(Component.literal("Все заклинания уже открыты."));
                return InteractionResultHolder.fail(heldStack);
            }

            // Шанс: старт 50%, -5% за шаг, минимум 10%
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

            // Синхронизируем состояние изученных заклинаний
            if (pPlayer instanceof net.minecraft.server.level.ServerPlayer sp) {
                PacketDistributor.sendToPlayer(sp, new SyncLearnedSpellsPacket(learned.serializeNBT(sp.registryAccess())));
            }

            return InteractionResultHolder.success(heldStack);
        }

        return InteractionResultHolder.pass(heldStack);
    }

    private void appendTagItemsInRegistryOrder(List<Item> out, TagKey<Item> tagKey) {
        Optional<List<Holder<Item>>> opt = BuiltInRegistries.ITEM.getTag(tagKey).map(tag -> tag.stream().toList());
        opt.ifPresent(list -> list.forEach(holder -> out.add(holder.value())));
    }
}