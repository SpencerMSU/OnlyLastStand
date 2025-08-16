package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.util.ModTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpellSlot extends SlotItemHandler {

    public SpellSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        // Теперь в этот слот можно положить только предметы с тегом "spells"
        return stack.is(ModTags.Items.SPELLS);
    }
}