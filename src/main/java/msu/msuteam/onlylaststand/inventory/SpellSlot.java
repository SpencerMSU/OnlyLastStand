package msu.msuteam.onlylaststand.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpellSlot extends SlotItemHandler {

    public SpellSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    /**
     * Этот метод определяет, можно ли поместить предмет в данный слот.
     */
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        // ЗАДЕЛ НА БУДУЩЕЕ:
        // Сейчас эта проверка будет пропускать все.
        // Когда у нас появятся предметы-заклинания, мы добавим им тег
        // и будем проверять его здесь. Например:
        // return stack.is(ModItemTags.SPELLS);

        // А пока что, для теста, разрешим класть любой предмет.
        return true;
    }
}