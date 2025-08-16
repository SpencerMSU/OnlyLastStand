package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.network.SpawnParticlesPacket;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class SparkSpell extends SpellItem {
    public SparkSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 5, 10);
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        Vec3 lookAngle = player.getLookAngle();
        for (int i = 0; i < 15; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;

            PacketDistributor.sendToPlayer((ServerPlayer) player, new SpawnParticlesPacket(0,
                    player.getX() + lookAngle.x * 1.5,
                    player.getEyeY() + lookAngle.y,
                    player.getZ() + lookAngle.z * 1.5,
                    lookAngle.x * 0.2 + offsetX,
                    lookAngle.y * 0.2 + offsetY,
                    lookAngle.z * 0.2 + offsetZ));
        }
        level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 1.5F);
    }
}