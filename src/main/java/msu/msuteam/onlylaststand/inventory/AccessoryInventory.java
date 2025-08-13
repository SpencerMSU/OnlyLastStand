package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.event.PlayerEventHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AccessoryInventory extends ItemStackHandler {
    public static final int SLOTS = 9;
    private Player player;

    public AccessoryInventory() {
        super(SLOTS);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.player != null && !this.player.level().isClientSide) {
            PlayerEventHandler.updateAllPlayerModifiers(this.player);
        }
    }
}