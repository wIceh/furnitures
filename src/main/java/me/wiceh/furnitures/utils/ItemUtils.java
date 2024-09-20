package me.wiceh.furnitures.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HexFormat;

public class ItemUtils {

    public static String serializeInventory(Inventory inventory) {
        StringBuilder builder = new StringBuilder();
        ItemStack[] var2 = inventory.getContents();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack item = var2[var4];
            builder.append(item == null ? "air" : HexFormat.of().formatHex(item.serializeAsBytes())).append('#');
        }

        return builder.toString();
    }

    public static ItemStack[] deserializeInventory(String string, int size) {
        String[] hexSplit = string.split(String.valueOf('#'));
        ItemStack[] itemStacks = new ItemStack[size];

        for(int i = 0; i < hexSplit.length; ++i) {
            String hexOrNull = hexSplit[i];
            if (!hexOrNull.equals("air")) {
                itemStacks[i] = ItemStack.deserializeBytes(HexFormat.of().parseHex(hexOrNull));
            }
        }

        return itemStacks;
    }
}
