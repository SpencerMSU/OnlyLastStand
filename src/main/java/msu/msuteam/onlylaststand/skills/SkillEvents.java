package msu.msuteam.onlylaststand.skills;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class SkillEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerSkills oldSkills = event.getOriginal().getData(ModAttachments.PLAYER_SKILLS);
            Player newPlayer = event.getEntity();
            newPlayer.getData(ModAttachments.PLAYER_SKILLS).deserializeNBT(newPlayer.registryAccess(), oldSkills.serializeNBT(newPlayer.registryAccess()));
        }
    }

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            if (event.getEntity() instanceof Monster) {
                skills.getSkill(PlayerSkill.COMBAT).addExperience(player, 10);
            } else {
                skills.getSkill(PlayerSkill.COMBAT).addExperience(player, 2);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            BlockState state = event.getState();

            if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                skills.getSkill(PlayerSkill.FARMING).addExperience(player, 15);

                int farmingLevel = skills.getSkill(PlayerSkill.FARMING).getLevel();
                double doubleChance = farmingLevel * 0.0017;
                if (Math.random() < doubleChance) {
                    event.setCanceled(true);
                    Block.dropResources(state, player.level(), event.getPos(), player.level().getBlockEntity(event.getPos()), player, player.getMainHandItem());
                    Block.dropResources(state, player.level(), event.getPos(), player.level().getBlockEntity(event.getPos()), player, player.getMainHandItem());
                    player.level().destroyBlock(event.getPos(), false);
                }
            }
            if (state.is(Blocks.PUMPKIN) || state.is(Blocks.MELON)) {
                skills.getSkill(PlayerSkill.FARMING).addExperience(player, 15);
            }

            boolean isOre = state.is(BlockTags.COAL_ORES) || state.is(BlockTags.COPPER_ORES) || state.is(BlockTags.DIAMOND_ORES) ||
                    state.is(BlockTags.EMERALD_ORES) || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.IRON_ORES) ||
                    state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.REDSTONE_ORES);

            if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.BASE_STONE_NETHER) || isOre) {
                skills.getSkill(PlayerSkill.MINING).addExperience(player, 3);
            }
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            skills.getSkill(PlayerSkill.CRAFTING).addExperience(player, 10);

            int craftingLevel = skills.getSkill(PlayerSkill.CRAFTING).getLevel();
            double doubleChance = craftingLevel * 0.0003;
            if (Math.random() < doubleChance) {
                player.getInventory().add(event.getCrafting().copy());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getAmount() > 0) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            int xpAmount = (int) (event.getAmount() / 2.0f * 50.0f);
            if (xpAmount > 0) {
                skills.getSkill(PlayerSkill.VITALITY).addExperience(player, xpAmount);
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.getOwner() instanceof ServerPlayer player && event.getRayTraceResult() instanceof EntityHitResult) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            skills.getSkill(PlayerSkill.ACCURACY).addExperience(player, 20);
        }
    }
}