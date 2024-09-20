package me.wiceh.furnitures.listeners;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.events.FurniturePlaceEvent;
import me.wiceh.furnitures.objects.Furniture;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FurnitureListeners implements Listener {

    private final Furnitures plugin;

    public FurnitureListeners(Furnitures plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurniturePlace(FurniturePlaceEvent event) {
        Furniture furniture = event.getFurniture();
    }
}
