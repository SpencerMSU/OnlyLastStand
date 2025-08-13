package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SkillsMenu extends AbstractContainerMenu {

    // ИСПРАВЛЕНО: Предоставляем ОБА конструктора, чтобы удовлетворить все требования
    public SkillsMenu(int pContainerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(pContainerId, inventory);
    }

    public SkillsMenu(int pContainerId, Inventory inventory) {
        super(ModMenuTypes.SKILLS_MENU.get(), pContainerId);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}