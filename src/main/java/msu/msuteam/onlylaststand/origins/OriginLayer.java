package msu.msuteam.onlylaststand.origins;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class OriginLayer {
    private final ResourceLocation id;
    private final Component name;
    private final List<ResourceLocation> origins;

    public OriginLayer(ResourceLocation id, Component name, List<ResourceLocation> origins) {
        this.id = id;
        this.name = name;
        this.origins = origins;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getName() {
        return name;
    }

    public List<ResourceLocation> getOrigins() {
        return origins;
    }
}