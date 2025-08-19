package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import msu.msuteam.onlylaststand.item.spells.SpellItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
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

        int activeSlotsX = 54;
        int activeSlotsY = 18;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                this.addSlot(new SpellSlot(spellInventory, col + row * 2, activeSlotsX + col * 18, activeSlotsY + row * 18, this.player));
            }
        }
    }

    @Override
    public void clicked(int pSlotId, int pButton, @NotNull ClickType pClickType, @NotNull Player pPlayer) {
        ItemStack carried = this.getCarried();

        // 1) ВНЕ ОКНА/ВЫБРОС: не материализуем виртуальные спеллы
        if (!carried.isEmpty() && carried.getItem() instanceof SpellItem && (pClickType == ClickType.THROW || pSlotId == -999)) {
            this.setCarried(ItemStack.EMPTY);
            return;
        }

        // 2) Наши слоты заклинаний: "ставим, не забираем старое"
        if (pSlotId >= 0 && pSlotId < this.slots.size()) {
            Slot slot = this.getSlot(pSlotId);

            if (slot.container instanceof SpellInventory) {
                // Переносим с курсора в слот (не забирая старое из слота на курсор)
                if (pClickType == ClickType.PICKUP && !carried.isEmpty()) {
                    if (carried.getItem() instanceof SpellItem && slot.mayPlace(carried)) {
                        slot.set(carried.copy());       // просто перезаписываем содержимое слота
                        this.setCarried(ItemStack.EMPTY); // очищаем курсор
                        slot.setChanged();
                        this.broadcastChanges();
                        return;
                    } else {
                        // Ничего не делаем, если предмет не подходит
                        return;
                    }
                }

                // Если курсор пуст — обычное поведение "взять из слота на курсор" оставляем
                if (pClickType == ClickType.PICKUP && carried.isEmpty()) {
                    ItemStack inSlot = slot.getItem();
                    if (!inSlot.isEmpty()) {
                        this.setCarried(inSlot.copy());
                        slot.set(ItemStack.EMPTY);
                        slot.setChanged();
                        this.broadcastChanges();
                        return;
                    }
                }

                // Блокируем SWAP/SHIFT-мувы для виртуальных слотов
                if (pClickType == ClickType.SWAP) {
                    return;
                }
            }
        }

        // 3) Остальное — по умолчанию
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    @Override
    public void removed(Player pPlayer) {
        // ВАЖНО: при закрытии меню чистим виртуальный предмет на курсоре,
        // чтобы он не попадал в инвентарь после ESC.
        ItemStack carried = this.getCarried();
        if (!carried.isEmpty() && carried.getItem() instanceof SpellItem) {
            this.setCarried(ItemStack.EMPTY);
        }
        super.removed(pPlayer);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        // Запрещаем shift-клик переносы
        return ItemStack.EMPTY;
    }
}