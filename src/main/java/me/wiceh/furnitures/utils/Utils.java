package me.wiceh.furnitures.utils;

import me.wiceh.furnitures.constants.Icon;
import me.wiceh.furnitures.constants.Palette;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.Random;

import static net.kyori.adventure.text.Component.text;

public class Utils {

    public static String color(String text) {
        String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static void sendMessage(Player player, Icon icon, String message) {
        String hex = "";

        switch (icon) {
            case ERROR_BLUE -> hex = Palette.BLUE.getHex();
            case ERROR_GREEN -> hex = Palette.GREEN.getHex();
            case ERROR_RED -> hex = Palette.RED.getHex();
            case ERROR_YELLOW -> hex = Palette.YELLOW.getHex();
        }

        player.sendMessage(text(icon.getIcon() + " ").append(text(message).color(TextColor.fromHexString(hex))));
    }

    public static String generateId(int length) {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);

            builder.append(randomChar);
        }

        return builder.toString();
    }
}
