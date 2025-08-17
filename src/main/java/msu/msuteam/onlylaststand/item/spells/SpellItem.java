package msu.msuteam.onlylaststand.item.spells;

import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import msu.msuteam.onlylaststand.network.DisplayNotificationPacket;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
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

    // Этот метод теперь просто "обертка" для стандартного использования
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) {
            doCast((ServerPlayer) pPlayer, pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    // ИСПРАВЛЕНО: Вся логика теперь в этом "чистом" методе, который вызывается напрямую
    public void doCast(ServerPlayer serverPlayer, ItemStack stack) {
        PlayerMana mana = serverPlayer.getData(ModAttachments.PLAYER_MANA);
        PlayerSkills skills = serverPlayer.getData(ModAttachments.PLAYER_SKILLS);
        int magicLevel = skills.getSkill(PlayerSkill.MAGIC).getLevel();
        float discount = 1.0f - (magicLevel * 0.005f);
        int finalManaCost = (int) Math.max(1, this.manaCost * discount);

        if (mana.getCurrentMana() >= finalManaCost) {
            if (!serverPlayer.getCooldowns().isOnCooldown(this)) {
                cast(serverPlayer.level(), serverPlayer, stack);
                mana.consume(finalManaCost);
                skills.getSkill(PlayerSkill.MAGIC).addExperience(serverPlayer, this.rarity.getXpValue());
                serverPlayer.getCooldowns().addCooldown(this, this.cooldownTicks);
            } else {
                PacketDistributor.sendToPlayer(serverPlayer, new DisplayNotificationPacket(Component.literal("Заклинание перезаряжается!")));
            }
        } else {
            PacketDistributor.sendToPlayer(serverPlayer, new DisplayNotificationPacket(Component.literal("Недостаточно маны!")));
            serverPlayer.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
        }
    }


    protected abstract void cast(Level level, Player player, ItemStack stack);

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("Стоимость: " + this.manaCost + " маны"));
        pTooltipComponents.add(Component.literal("Перезарядка: " + (this.cooldownTicks / 20.0) + "с"));
        pTooltipComponents.add(this.rarity.getDisplayName());
    }
}