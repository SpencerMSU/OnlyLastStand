package msu.msuteam.onlylaststand.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.AccessoryInventory;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.inventory.SpellInventory;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.item.spells.fire_school.FireShieldSpell;
import msu.msuteam.onlylaststand.item.spells.fire_school.SatansHelpSpell;
import msu.msuteam.onlylaststand.network.SyncLearnedSpellsPacket;
import msu.msuteam.onlylaststand.network.SyncSpellsPacket;
import msu.msuteam.onlylaststand.skills.PlayerLearnedSpells;
import msu.msuteam.onlylaststand.skills.PlayerSkill;
import msu.msuteam.onlylaststand.skills.PlayerSkills;
import msu.msuteam.onlylaststand.util.CollectionType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class PlayerEventHandler {

    private static final Map<UUID, Multimap<Holder<Attribute>, AttributeModifier>> lastAppliedModifiers = new HashMap<>();
    private static final ResourceLocation FIRE_SET_NETHER_BONUS_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "fire_set_nether_bonus");
    private static final ResourceLocation WATER_SET_SWIM_SPEED_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "water_set_swim_speed");

    private static final ResourceLocation VITALITY_HEALTH_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "vitality_health");
    private static final ResourceLocation ACCURACY_BONUS_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "accuracy_draw_speed");

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            if (player.tickCount % 1200 == 0) { // Проверка раз в 60 секунд (1200 тиков)
                PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
                SpellInventory spellInventory = player.getData(ModAttachments.SPELL_INVENTORY);
                int unlockedSlots = skills.getUnlockedSpellSlots();

                for (int i = unlockedSlots; i < spellInventory.getSlots(); i++) {
                    if (!spellInventory.getStackInSlot(i).isEmpty()) {
                        spellInventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
            // Вызываем тики для заклинаний, которым это нужно
            FireShieldSpell.onPlayerTick(player);
            SatansHelpSpell.onPlayerTick(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        updateAllPlayerModifiers(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        updateAllPlayerModifiers(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        updateAllPlayerModifiers(event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Player player) {
            AccessoryInventory inventory = player.getData(ModAttachments.ACCESSORY_INVENTORY);
            inventory.setPlayer(player);

            player.getData(ModAttachments.PLAYER_MANA).setPlayer(player);

            if (player instanceof ServerPlayer serverPlayer) {
                SpellInventory spellInventory = serverPlayer.getData(ModAttachments.SPELL_INVENTORY);
                PacketDistributor.sendToPlayer(serverPlayer, new SyncSpellsPacket(spellInventory.serializeNBT(serverPlayer.registryAccess())));

                PlayerLearnedSpells learnedSpells = serverPlayer.getData(ModAttachments.PLAYER_LEARNED_SPELLS);
                PacketDistributor.sendToPlayer(serverPlayer, new SyncLearnedSpellsPacket(learnedSpells.serializeNBT(serverPlayer.registryAccess())));
            }

            updateAllPlayerModifiers(player);
        }
    }

    public static void updateAllPlayerModifiers(Player player) {
        if (player.level().isClientSide) return;

        Multimap<Holder<Attribute>, AttributeModifier> lastMods = lastAppliedModifiers.get(player.getUUID());
        if (lastMods != null && !lastMods.isEmpty()) {
            player.getAttributes().removeAttributeModifiers(lastMods);
        }

        Multimap<Holder<Attribute>, AttributeModifier> currentMods = HashMultimap.create();
        AccessoryInventory inventory = player.getData(ModAttachments.ACCESSORY_INVENTORY);
        if (inventory == null) return;

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() instanceof AccessoryItem accessory) {
                accessory.getAccessoryAttributeModifiers(stack).modifiers().forEach(entry -> {
                    currentMods.put(entry.attribute(), entry.modifier());
                });
                accessory.onAccessoryTick(stack, player);
            }
        }

        if (isWearingFullSet(player, CollectionType.FIRE) && player.level().dimension().equals(Level.NETHER)) {
            AttributeModifier netherBonus = new AttributeModifier(
                    FIRE_SET_NETHER_BONUS_ID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
            currentMods.put(Attributes.ATTACK_DAMAGE, netherBonus);
        } else if (isWearingFullSet(player, CollectionType.WATER)) {
            AttributeModifier swimSpeedBonus = new AttributeModifier(
                    WATER_SET_SWIM_SPEED_ID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
            currentMods.put(NeoForgeMod.SWIM_SPEED, swimSpeedBonus);
        }

        PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
        if (skills != null) {
            int vitalityLevel = skills.getSkill(PlayerSkill.VITALITY).getLevel();
            double healthBonus = (vitalityLevel / 10) * 1.0;
            if (healthBonus > 0) {
                currentMods.put(Attributes.MAX_HEALTH, new AttributeModifier(VITALITY_HEALTH_ID, healthBonus, AttributeModifier.Operation.ADD_VALUE));
            }

            int accuracyLevel = skills.getSkill(PlayerSkill.ACCURACY).getLevel();
            double drawSpeedBonus = accuracyLevel * 0.005;
            if (drawSpeedBonus > 0) {
                currentMods.put(Attributes.ATTACK_SPEED, new AttributeModifier(ACCURACY_BONUS_ID, drawSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        }

        if (!currentMods.isEmpty()) {
            player.getAttributes().addTransientAttributeModifiers(currentMods);
        }

        handlePotionEffects(player);
        lastAppliedModifiers.put(player.getUUID(), currentMods);
    }

    private static void handlePotionEffects(Player player) {
        player.removeEffect(MobEffects.FIRE_RESISTANCE);
        player.removeEffect(MobEffects.WATER_BREATHING);

        if (isWearingFullSet(player, CollectionType.WATER)) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, -1, 0, false, false, true));
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            PlayerSkills skills = player.getData(ModAttachments.PLAYER_SKILLS);
            if (skills != null) {
                int combatLevel = skills.getSkill(PlayerSkill.COMBAT).getLevel();
                float damageBonus = 1.0f + (combatLevel * 0.0015f);
                event.setAmount(event.getAmount() * damageBonus);
            }
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (isWearingFullSet(player, CollectionType.FIRE)) {
            if (event.getSource().is(DamageTypes.FALL)) {
                event.setAmount(event.getAmount() * 1.13f);
                return;
            }

            if (event.getSource().getEntity() instanceof Ghast || event.getSource().getEntity() instanceof Blaze) {
                event.setAmount(event.getAmount() * 0.65f);
            }
            else if (player.level().dimension().equals(Level.NETHER) && event.getSource().getEntity() instanceof Mob) {
                event.setAmount(event.getAmount() * 0.85f);
            }
            else if (event.getSource().is(DamageTypes.LAVA)) {
                event.setAmount(event.getAmount() * 0.30f);
            }
        }

        if (isWearingFullSet(player, CollectionType.WATER)) {
            if (event.getSource().getEntity() instanceof Player sourcePlayer && event.getEntity() instanceof LivingEntity target) {
                if (sourcePlayer.isInWaterOrRain() && target.isInWaterOrRain()) {
                    event.setAmount(event.getAmount() * 1.5f);
                }
            }
        }
    }

    // ИСПРАВЛЕНО: Используем LivingDamageEvent
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        // ИСПРАВЛЕНО: Правильный способ получить атакующего
        if (event.getSource().getEntity() instanceof Player player) {
            // Клинки Огня
            if (player.getPersistentData().contains("BladesOfFireTicks")) {
                int currentFire = event.getEntity().getRemainingFireTicks();
                event.getEntity().setRemainingFireTicks(currentFire + (1200)); // +1 минута горения
            }
            // Помощь Сатаны
            if (player.getPersistentData().contains("SatansHelpTicks")) {
                event.getEntity().getPersistentData().putInt("SatanicFireTicks", 200); // 10 секунд особого горения
            }
        }
    }

    public static boolean isWearingFullSet(Player player, CollectionType collection) {
        AccessoryInventory inventory = player.getData(ModAttachments.ACCESSORY_INVENTORY);
        if (inventory == null) return false;
        for (int i = 0; i < AccessoryInventory.SLOTS; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!(stack.getItem() instanceof AccessoryItem accessory) || accessory.getCollectionType() != collection) {
                return false;
            }
        }
        return true;
    }
}