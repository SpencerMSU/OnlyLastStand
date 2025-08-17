package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.skills.PlayerSkills;
import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpellSlot extends SlotItemHandler {

    private final Player player;

    public SpellSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        // --- ОТЛАДКА НА СЕРВЕРЕ ---
        if (!player.level().isClientSide()) {
            player.sendSystemMessage(Component.literal("--- Проверка слота ---"));
            if (!stack.is(ModTags.Items.SPELLS)) {
                player.sendSystemMessage(Component.literal("§cОШИБКА: Предмет не имеет тега 'spells'."));
                return false;
            }

            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            int unlockedSlots = skills.getUnlockedSpellSlots();
            player.sendSystemMessage(Component.literal("§eИНФО: Открыто слотов: " + unlockedSlots + ", Индекс этого слота: " + this.index));

            if (this.index >= unlockedSlots) {
                player.sendSystemMessage(Component.literal("§cОШИБКА: Этот слот заблокирован."));
                return false;
            }
            player.sendSystemMessage(Component.literal("§aУСПЕХ: Все проверки пройдены."));
        }
        // -------------------------

        PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
        int unlockedSlots = skills.getUnlockedSpellSlots();

        return stack.is(ModTags.Items.SPELLS) && this.index < unlockedSlots;
    }
}