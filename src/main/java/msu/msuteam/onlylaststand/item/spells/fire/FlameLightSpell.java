package msu.msuteam.onlylaststand.item.spells.fire;

import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.network.SpawnParticlesPacket;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class FlameLightSpell extends SpellItem {
    public FlameLightSpell(Properties pProperties) {
        super(pProperties, Rarity.COMMON, 15, 40);
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(10)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        Vec3 targetPos = ray.getLocation();

        for (int i = 0; i < 20; i++) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SpawnParticlesPacket(1, targetPos.x, targetPos.y, targetPos.z, 0, 0.05, 0));
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}