package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SpellScrollItem extends Item {

    private final Supplier<Item> spellItem;

    public SpellScrollItem(Properties pProperties, Supplier<Item> spellItem) {
        super(pProperties);
        this.spellItem = spellItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            SpellInventory spellInventory = pPlayer.getData(ModAttachments.SPELL_INVENTORY);
            ItemStack spellItemStack = new ItemStack(this.spellItem.get());

            // Проверяем, есть ли уже такое заклинание
            for (int i = 0; i < spellInventory.getSlots(); i++) {
                if (spellInventory.getStackInSlot(i).is(this.spellItem.get())) {
                    pPlayer.sendSystemMessage(Component.literal("Вы уже знаете это заклинание!"));
                    return InteractionResultHolder.fail(heldStack);
                }
            }

            // Пытаемся добавить в инвентарь заклинаний
            for (int i = 0; i < spellInventory.getSlots(); i++) {
                if (spellInventory.getStackInSlot(i).isEmpty()) {
                    spellInventory.setStackInSlot(i, spellItemStack);
                    pPlayer.sendSystemMessage(Component.literal("Вы изучили новое заклинание: " + spellItemStack.getHoverName().getString()));
                    pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8f, 1.0f);
                    if (!pPlayer.isCreative()) {
                        heldStack.shrink(1);
                    }
                    return InteractionResultHolder.success(heldStack);
                }
            }

            // Если нет места
            pPlayer.sendSystemMessage(Component.literal("Нет места для изучения нового заклинания!"));
            return InteractionResultHolder.fail(heldStack);
        }
        return InteractionResultHolder.pass(heldStack);
    }
}