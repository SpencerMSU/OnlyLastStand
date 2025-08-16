package msu.msuteam.onlylaststand.origins;

import net.minecraft.world.entity.player.Player;

public interface Power {
    void onAdded(Player player);
    void onRemoved(Player player);
}