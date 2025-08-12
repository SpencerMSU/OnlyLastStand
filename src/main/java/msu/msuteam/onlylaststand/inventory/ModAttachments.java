package msu.msuteam.onlylaststand.inventory;

import msu.msuteam.onlylaststand.OnlyLastStand;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, OnlyLastStand.MODID);

    public static final Supplier<AttachmentType<AccessoryInventory>> ACCESSORY_INVENTORY =
            ATTACHMENT_TYPES.register("accessory_inventory", () -> AttachmentType.serializable(AccessoryInventory::new).build());
}