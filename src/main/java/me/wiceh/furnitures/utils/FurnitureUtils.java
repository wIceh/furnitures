package me.wiceh.furnitures.utils;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.objects.Furniture;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FurnitureUtils {

    private final Furnitures plugin;

    public FurnitureUtils(Furnitures plugin) {
        this.plugin = plugin;
    }

    public List<Furniture> getFurnitures() {
        List<Furniture> furnitures = new ArrayList<>();

        ConfigurationSection furnituresSection = plugin.getConfig().getConfigurationSection("furnitures");
        if (furnituresSection == null) return furnitures;
        if (furnituresSection.getKeys(false).isEmpty()) return furnitures;

        for (String key : furnituresSection.getKeys(false)) {
            ConfigurationSection section = furnituresSection.getConfigurationSection(key);
            if (section == null) continue;

            furnitures.add(new Furniture(
                    key,
                    section.getString("display-name"),
                    Material.valueOf(section.getString("material")),
                    section.getInt("custom-model-data")
            ));
        }

        return furnitures;
    }

    public Optional<Furniture> getFurniture(String id) {
        for (Furniture furniture : getFurnitures()) {
            if (!furniture.getId().equals(id)) continue;

            return Optional.of(furniture);
        }

        return Optional.empty();
    }

    public ItemStack getFurnitureItem(Furniture furniture) {
        ItemStack item = new ItemStack(furniture.getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(furniture.getDisplayName()));
        meta.setCustomModelData(furniture.getCustomModelData());
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "furniture"), PersistentDataType.STRING, furniture.getId());
        item.setItemMeta(meta);

        return item;
    }

    public boolean isPlaceableOnFloor(Furniture furniture) {
        return plugin.getConfig().getBoolean("furnitures." + furniture.getId() + ".floor");
    }

    public boolean isPlaceableOnWalls(Furniture furniture) {
        return plugin.getConfig().getBoolean("furnitures." + furniture.getId() + ".walls");
    }

    public boolean isPlaceableOnCeiling(Furniture furniture) {
        return plugin.getConfig().getBoolean("furnitures." + furniture.getId() + ".ceiling");
    }

    public boolean smallArmorStand(Furniture furniture) {
        return plugin.getConfig().getBoolean("furnitures." + furniture.getId() + ".small");
    }
}
