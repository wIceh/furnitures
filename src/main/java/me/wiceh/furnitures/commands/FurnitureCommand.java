package me.wiceh.furnitures.commands;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.constants.Icon;
import me.wiceh.furnitures.objects.Furniture;
import me.wiceh.furnitures.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class FurnitureCommand implements CommandExecutor, TabCompleter {

    private final Furnitures plugin;

    public FurnitureCommand(Furnitures plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return List.of();

        if (args.length == 1) {
            return List.of("get", "reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            List<String> names = new ArrayList<>();

            for (Furniture furniture : plugin.getFurnitureUtils().getFurnitures()) {
                names.add(furniture.getId());
            }

            return names;
        }

        return List.of();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            if (!player.hasPermission("furnitures.get")) {
                Utils.sendMessage(player, Icon.ERROR_RED, "Non hai il permesso.");
                return true;
            }

            String id = args[1];
            Optional<Furniture> optionalFurniture = plugin.getFurnitureUtils().getFurniture(id);
            if (optionalFurniture.isEmpty()) {
                Utils.sendMessage(player, Icon.ERROR_YELLOW, "Furniture non trovata.");
                return true;
            }

            Furniture furniture = optionalFurniture.get();
            ItemStack item = plugin.getFurnitureUtils().getFurnitureItem(furniture);

            player.getInventory().addItem(item);
            Utils.sendMessage(player, Icon.ERROR_GREEN, "Furniture ottenuta con successo.");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("furnitures.reload")) {
                Utils.sendMessage(player, Icon.ERROR_RED, "Non hai il permesso.");
                return true;
            }

            plugin.reloadConfig();
            Utils.sendMessage(player, Icon.ERROR_GREEN, "Config ricaricato correttamente.");
        } else {
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String command) {
        player.sendMessage(text(""));
        player.sendMessage(text("§6§lFURNITURES"));
        player.sendMessage(text(" §8| §e/" + command + " get <id>"));
        player.sendMessage(text(" §8| §e/" + command + " reload"));
        player.sendMessage(text(""));
    }
}
