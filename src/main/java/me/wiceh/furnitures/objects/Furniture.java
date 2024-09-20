package me.wiceh.furnitures.objects;

import org.bukkit.Material;

public class Furniture {

    private final String id;
    private final String displayName;
    private final Material material;
    private final int customModelData;

    public Furniture(String id, String displayName, Material material, int customModelData) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.customModelData = customModelData;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }
}
