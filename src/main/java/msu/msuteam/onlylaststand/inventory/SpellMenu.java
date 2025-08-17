package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellMenu extends AbstractContainerMenu {

    public final Player player;

    public SpellMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory);
    }

    public SpellMenu(int pContainerId, Inventory playerInventory) {
        super(ModMenuTypes.SPELL_MENU.get(), pContainerId);
        this.player = playerInventory.player;
        SpellInventory spellInventory = this.player.getData(ModAttachments.SPELL_INVENTORY);

        // Создаем ТОЛЬКО 10 активных слотов, которые существуют на сервере
        int activeSlotsX = 54;
        int activeSlotsY = 18;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                this.addSlot(new SpellSlot(spellInventory, col + row * 2, activeSlotsX + col * 18, activeSlotsY + row * 18, this.player));
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    // Shift-клик нам не нужен, т.к. взаимодействие идет через перетаскивание
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }
}