package me.wiceh.furnitures;

import me.wiceh.furnitures.commands.FurnitureCommand;
import me.wiceh.furnitures.listeners.FurnitureListeners;
import me.wiceh.furnitures.listeners.Listeners;
import me.wiceh.furnitures.utils.FurnitureUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Furnitures extends JavaPlugin {

    private FurnitureUtils furnitureUtils;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.furnitureUtils = new FurnitureUtils(this);

        getCommand("furniture").setExecutor(new FurnitureCommand(this));
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new FurnitureListeners(this), this);
    }

    public FurnitureUtils getFurnitureUtils() {
        return furnitureUtils;
    }
}
