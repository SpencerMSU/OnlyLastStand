package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import msu.msuteam.onlylaststand.network.DisplayNotificationPacket;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public abstract class SpellItem extends Item {

    protected final Rarity rarity;
    protected final int manaCost;
    protected final int cooldownTicks;

    public SpellItem(Properties pProperties, Rarity rarity, int manaCost, int cooldownTicks) {
        super(pProperties.stacksTo(1));
        this.rarity = rarity;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        PlayerMana mana = pPlayer.getData(ModAttachments.PLAYER_MANA);

        if (!pLevel.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) pPlayer;
            if (mana.getCurrentMana() >= this.manaCost) {
                if (!pPlayer.getCooldowns().isOnCooldown(this)) {
                    cast(pLevel, pPlayer, itemStack);
                    mana.consume(this.manaCost);
                    pPlayer.getCooldowns().addCooldown(this, this.cooldownTicks);
                    return InteractionResultHolder.success(itemStack);
                } else {
                    PacketDistributor.sendToPlayer(serverPlayer, new DisplayNotificationPacket(Component.literal("Заклинание перезаряжается!")));
                    return InteractionResultHolder.fail(itemStack);
                }
            } else {
                PacketDistributor.sendToPlayer(serverPlayer, new DisplayNotificationPacket(Component.literal("Недостаточно маны!")));
                pPlayer.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
                return InteractionResultHolder.fail(itemStack);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    protected abstract void cast(Level level, Player player, ItemStack stack);

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("Стоимость: " + this.manaCost + " маны"));
        pTooltipComponents.add(Component.literal("Перезарядка: " + (this.cooldownTicks / 20.0) + "с"));
        pTooltipComponents.add(this.rarity.getDisplayName());
    }
}