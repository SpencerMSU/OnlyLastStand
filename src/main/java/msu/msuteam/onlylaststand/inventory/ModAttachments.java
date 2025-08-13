package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.OnlyLastStand;
import msu.msuteam.onlylaststand.magic.PlayerMana;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, OnlyLastStand.MODID);

    public static final Supplier<AttachmentType<AccessoryInventory>> ACCESSORY_INVENTORY =
            ATTACHMENT_TYPES.register("accessory_inventory", () -> AttachmentType.serializable(AccessoryInventory::new).build());

    public static final Supplier<AttachmentType<PlayerMana>> PLAYER_MANA =
            ATTACHMENT_TYPES.register("player_mana", () -> AttachmentType.serializable(PlayerMana::new).build());

    public static final Supplier<AttachmentType<SpellInventory>> SPELL_INVENTORY =
            ATTACHMENT_TYPES.register("spell_inventory", () -> AttachmentType.serializable(SpellInventory::new).build());

}