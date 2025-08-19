package msu.msuteam.onlylaststand.item.spells.fire_school;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.item.spells.SpellItem;
import msu.msuteam.onlylaststand.util.Rarity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SatansHelpSpell extends SpellItem {

    private static final ResourceLocation HEALTH_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "satans_help_health");
    private static final ResourceLocation DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "satans_help_damage");
    public static final int DURATION_TICKS = 1200; // 1 минута

    public SatansHelpSpell(Properties pProperties) {
        super(pProperties, Rarity.DEMONIC, 300, 36000); // КД 30 минут
    }

    @Override
    protected void cast(Level level, Player player, ItemStack stack) {
        player.getPersistentData().putInt("SatansHelpTicks", DURATION_TICKS);

        AttributeModifier healthModifier = new AttributeModifier(HEALTH_MODIFIER_ID, 60.0, AttributeModifier.Operation.ADD_VALUE);
        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
        player.heal(60.0F);

        AttributeModifier damageModifier = new AttributeModifier(DAMAGE_MODIFIER_ID, 10.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(damageModifier);

        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 2));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, DURATION_TICKS, 0, false, false));

        BlockHitResult ray = level.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getLookAngle().scale(80)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 targetPos = ray.getLocation();
        BlockPos blockPos = BlockPos.containing(targetPos);

        if (level instanceof ServerLevel serverLevel) {
            level.playSound(null, player.blockPosition(), SoundEvents.WITHER_DEATH, SoundSource.PLAYERS, 2.0F, 0.5F);
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                serverLevel.getServer().execute(() -> {
                    level.playSound(null, blockPos, SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 4.0F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, targetPos.x, targetPos.y, targetPos.z, 10, 5, 5, 5, 0);
                    AABB area = new AABB(blockPos).inflate(10);
                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
                        entity.hurt(level.damageSources().indirectMagic(player, player), 300.0F);
                    }
                });
            }, 1, TimeUnit.SECONDS);
        }
    }

    public static void onPlayerTick(Player player) {
        if (player.getPersistentData().contains("SatansHelpTicks")) {
            int ticks = player.getPersistentData().getInt("SatansHelpTicks");
            if (ticks > 0) {
                player.getActiveEffects().removeIf(effect -> effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL);
                player.setAirSupply(player.getMaxAirSupply());
                player.getPersistentData().putInt("SatansHelpTicks", ticks - 1);
            } else {
                player.getPersistentData().remove("SatansHelpTicks");
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_MODIFIER_ID);
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_MODIFIER_ID);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
        }
    }
}