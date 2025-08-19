package msu.msuteam.onlylaststand.skills;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;
import java.util.Optional;

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
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {
            if (player.getPersistentData().contains("DevilsToolsTicks")) {
                ItemStack tool = player.getMainHandItem();
                if (tool.getItem() instanceof DiggerItem) {
                    event.setCanceled(true);
                    int radius = 1;
                    int depth = (tool.is(Items.NETHERITE_PICKAXE)) ? 1 : 0;
                    BlockPos origin = event.getPos();

                    for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -depth, -radius), origin.offset(radius, depth, radius))) {
                        BlockState state = player.level().getBlockState(pos);
                        if (!state.isAir() && tool.isCorrectToolForDrops(state)) {
                            List<ItemStack> drops = Block.getDrops(state, serverPlayer.serverLevel(), pos, player.level().getBlockEntity(pos), player, tool);
                            for (ItemStack drop : drops) {
                                // Новый способ получения рецепта (1.21.x): SingleRecipeInput вместо SimpleContainer
                                Optional<RecipeHolder<SmeltingRecipe>> recipe =
                                        player.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(drop), player.level());
                                if (recipe.isPresent()) {
                                    ItemStack result = recipe.get().value().getResultItem(player.level().registryAccess());
                                    if (!result.isEmpty()) {
                                        ItemStack finalDrop = result.copy();
                                        if (tool.is(Items.NETHERITE_PICKAXE) && Math.random() < 0.20) {
                                            finalDrop.grow(finalDrop.getCount());
                                        }
                                        Block.popResource(player.level(), pos, finalDrop);
                                    }
                                } else {
                                    Block.popResource(player.level(), pos, drop);
                                }
                            }
                            player.level().destroyBlock(pos, false, player);
                        }
                    }
                    return;
                }
            }

            PlayerSkills skills = serverPlayer.getData(ModAttachments.PLAYER_SKILLS);
            BlockState state = event.getState();

            if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                skills.getSkill(PlayerSkill.FARMING).addExperience(serverPlayer, 15);
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
                skills.getSkill(PlayerSkill.FARMING).addExperience(serverPlayer, 15);
            }

            boolean isOre = state.is(BlockTags.COAL_ORES) || state.is(BlockTags.COPPER_ORES) || state.is(BlockTags.DIAMOND_ORES) ||
                    state.is(BlockTags.EMERALD_ORES) || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.IRON_ORES) ||
                    state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.REDSTONE_ORES);

            if (isOre) {
                skills.getSkill(PlayerSkill.MINING).addExperience(serverPlayer, 3);
                int miningLevel = skills.getSkill(PlayerSkill.MINING).getLevel();
                double doubleChance = miningLevel * 0.0013;
                if (player.level().random.nextDouble() < doubleChance) {
                    List<ItemStack> drops = Block.getDrops(state, (ServerLevel) player.level(), event.getPos(), null, player, player.getMainHandItem());
                    for (ItemStack drop : drops) {
                        Block.popResource(player.level(), event.getPos(), drop);
                    }
                }
            } else if (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.BASE_STONE_NETHER)) {
                skills.getSkill(PlayerSkill.MINING).addExperience(serverPlayer, 3);
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

    // Заменяем устаревший LivingEvent.LivingTickEvent на EntityTickEvent.Post
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        if (!entity.level().isClientSide && entity.getPersistentData().contains("SatanicFireTicks")) {
            int ticks = entity.getPersistentData().getInt("SatanicFireTicks");
            if (ticks > 0) {
                if (entity.tickCount % 8 == 0) {
                    entity.hurt(entity.damageSources().lava(), 2.0f); // 1 сердце
                }
                entity.getPersistentData().putInt("SatanicFireTicks", ticks - 1);
            } else {
                entity.getPersistentData().remove("SatanicFireTicks");
            }
        }
    }
}