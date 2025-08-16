package msu.msuteam.onlylaststand.origins;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Origin {
    private final ResourceLocation id;
    private final Component name;
    private final Component description;
    private final ItemStack icon;
    private final List<ResourceLocation> powers;

    public Origin(ResourceLocation id, Component name, Component description, ItemStack icon, List<ResourceLocation> powers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.powers = powers;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<ResourceLocation> getPowers() {
        return powers;
    }
}