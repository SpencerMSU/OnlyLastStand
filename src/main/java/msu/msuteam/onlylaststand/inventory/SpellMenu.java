package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.core.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellMenu extends AbstractContainerMenu {
    public static final int ACTIVE_SPELL_SLOTS = 10;
    public final SpellInventory spellInventory;
    public final Container learnedSpellsContainer;

    // Этот конструктор вызывается клиентом
    public SpellMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, new SimpleContainer(54));
    }

    // Этот конструктор вызывается сервером
    public SpellMenu(int pContainerId, Inventory playerInventory) {
        this(pContainerId, playerInventory, new SimpleContainer(54));
    }

    // Основной конструктор
    public SpellMenu(int pContainerId, Inventory playerInventory, Container learnedSpellsContainer) {
        super(ModMenuTypes.SPELL_MENU.get(), pContainerId);
        this.spellInventory = playerInventory.player.getData(ModAttachments.SPELL_INVENTORY);
        this.learnedSpellsContainer = learnedSpellsContainer;

        // 10 активных слотов заклинаний
        int spellInvX = 62;
        int spellInvY = 18;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new SpellSlot(this.spellInventory, j + i * 2, spellInvX + j * 18, spellInvY + i * 18));
            }
        }

        // Слоты для "каталога" изученных заклинаний
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(this.learnedSpellsContainer, j + i * 9, -1000, 0));
            }
        }

        // Инвентарь игрока и хотбар
        int playerInvX = 8;
        int playerInvY = 124;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, playerInvX + j * 18, playerInvY + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, playerInvX + i * 18, playerInvY + 58));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotId >= ACTIVE_SPELL_SLOTS && slotId < ACTIVE_SPELL_SLOTS + learnedSpellsContainer.getContainerSize()) {
            if (clickType == ClickType.PICKUP) {
                Slot slot = this.slots.get(slotId);
                if (slot.hasItem()) {
                    this.setCarried(slot.getItem().copy());
                }
            }
            return;
        }
        super.clicked(slotId, dragType, clickType, player);
    }
}