package msu.msuteam.onlylaststand.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.inventory.AccessoryInventory;
import msu.msuteam.onlylaststand.inventory.ModAttachments;
import msu.msuteam.onlylaststand.item.accessories.AccessoryItem;
import msu.msuteam.onlylaststand.util.CollectionType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = OnlyLastStand.MODID)
public class PlayerEventHandler {

    private static final Map<UUID, Multimap<Holder<Attribute>, AttributeModifier>> lastAppliedModifiers = new HashMap<>();
    private static final ResourceLocation FIRE_SET_NETHER_BONUS_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "fire_set_nether_bonus");
    private static final ResourceLocation WATER_SET_SWIM_SPEED_ID = ResourceLocation.fromNamespaceAndPath(OnlyLastStand.MODID, "water_set_swim_speed");

    // Вызывается при входе в мир, респавне и смене измерения
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
            updateAllPlayerModifiers(player);
        }
    }

    // Главный метод, который вызывается, когда нужно обновить все баффы
    public static void updateAllPlayerModifiers(Player player) {
        if (player.level().isClientSide) return;

        // --- Удаляем старые атрибуты ---
        Multimap<Holder<Attribute>, AttributeModifier> lastMods = lastAppliedModifiers.get(player.getUUID());
        if (lastMods != null && !lastMods.isEmpty()) {
            player.getAttributes().removeAttributeModifiers(lastMods);
        }

        // --- Собираем новые атрибуты ---
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

        // --- Добавляем сетовые бонусы атрибутов ---
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

        // --- Применяем новые атрибуты ---
        if (!currentMods.isEmpty()) {
            player.getAttributes().addTransientAttributeModifiers(currentMods);
        }

        // --- Применяем эффекты зелий ---
        handlePotionEffects(player);

        // --- Сохраняем состояние ---
        lastAppliedModifiers.put(player.getUUID(), currentMods);
    }

    private static void handlePotionEffects(Player player) {
        // Сначала удаляем эффекты, чтобы они не накапливались, если сет снят
        player.removeEffect(MobEffects.FIRE_RESISTANCE);
        player.removeEffect(MobEffects.WATER_BREATHING);

        if (isWearingFullSet(player, CollectionType.FIRE)) {
            // Даем бесконечный эффект Огнестойкости
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, -1, 0, false, false, true));
        } else if (isWearingFullSet(player, CollectionType.WATER)) {
            // ИСПРАВЛЕНО: Даем бесконечный эффект "Подводное дыхание"
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, -1, 0, false, false, true));
        }
    }

    // Обработчик урона
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // Огненный сет
        if (event.getEntity() instanceof Player player && isWearingFullSet(player, CollectionType.FIRE)) {
            if (event.getSource().is(DamageTypes.IN_FIRE) || event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.LAVA)) {
                event.setCanceled(true);
                player.clearFire();
            }
        }

        // Водный сет
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity target && isWearingFullSet(player, CollectionType.WATER)) {
            if (player.isInWaterOrRain() && target.isInWaterOrRain()) {
                event.setAmount(event.getAmount() * 1.5f);
            }
        }
    }

    // Вспомогательный метод для проверки сета
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