package me.wiceh.furnitures.listeners;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.events.FurnitureBreakEvent;
import me.wiceh.furnitures.events.FurnitureInteractEvent;
import me.wiceh.furnitures.events.FurniturePlaceEvent;
import me.wiceh.furnitures.objects.Furniture;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;

import java.util.Optional;

public class Listeners implements Listener {

    private final Furnitures plugin;

    public Listeners(Furnitures plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurniturePlace(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null) return;
            if (!block.getType().isSolid()) return;
            BlockFace blockFace = event.getBlockFace();

            ItemStack item = event.getItem();
            if (item == null) return;

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            NamespacedKey key = new NamespacedKey(plugin, "furniture");
            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

            String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            Optional<Furniture> optionalFurniture = plugin.getFurnitureUtils().getFurniture(id);
            if (optionalFurniture.isEmpty()) return;

            Furniture furniture = optionalFurniture.get();

            event.setCancelled(true);

            Block b = block.getRelative(0, 1, 0);

            if (blockFace == BlockFace.UP) {
                // pavimento
                if (!plugin.getFurnitureUtils().isPlaceableOnFloor(furniture)) return;
            } else if (blockFace == BlockFace.DOWN) {
                // soffitto
                if (!plugin.getFurnitureUtils().isPlaceableOnCeiling(furniture)) return;
            } else {
                // muro
                if (!plugin.getFurnitureUtils().isPlaceableOnWalls(furniture)) return;
            }

            ItemStack clone = item.clone();
            clone.setAmount(1);

            ArmorStand armorStand = player.getWorld().spawn(b.getLocation().add(0.5, 0, 0.5), ArmorStand.class);
            FurniturePlaceEvent furniturePlaceEvent = new FurniturePlaceEvent(player, armorStand, furniture);
            Bukkit.getServer().getPluginManager().callEvent(furniturePlaceEvent);
            if (furniturePlaceEvent.isCancelled()) {
                armorStand.remove();
                return;
            }
            armorStand.setVisible(false);
            armorStand.setSmall(plugin.getFurnitureUtils().smallArmorStand(furniture));
            armorStand.getEquipment().setHelmet(clone);
            armorStand.setDisabledSlots(EquipmentSlot.HEAD);
            armorStand.setGravity(false);
            armorStand.setHeadPose(new EulerAngle(
                    0,
                    Math.toRadians(player.getLocation().getYaw()),
                    0
            ));
            armorStand.setRotation(0, player.getLocation().getYaw());
            armorStand.getPersistentDataContainer().set(new NamespacedKey(plugin, "furniture"), PersistentDataType.STRING, furniture.getId());
            item.setAmount(item.getAmount() - 1);
        }
    }

    @EventHandler
    public void onFurnitureBreak(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof ArmorStand armorStand)) return;

        NamespacedKey key = new NamespacedKey(plugin, "furniture");
        if (!armorStand.getPersistentDataContainer().has(key, PersistentDataType.STRING))
            return;

        String id = armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        Optional<Furniture> optionalFurniture = plugin.getFurnitureUtils().getFurniture(id);
        if (optionalFurniture.isEmpty()) return;

        Furniture furniture = optionalFurniture.get();

        ItemStack helmet = armorStand.getEquipment().getHelmet();
        if (helmet == null) return;

        FurnitureBreakEvent furnitureBreakEvent = new FurnitureBreakEvent(player, armorStand, furniture);
        Bukkit.getServer().getPluginManager().callEvent(furnitureBreakEvent);
        if (furnitureBreakEvent.isCancelled()) return;
        armorStand.remove();
        player.getInventory().addItem(helmet);
    }

    @EventHandler
    public void onFurnitureInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof ArmorStand armorStand)) return;

        NamespacedKey key = new NamespacedKey(plugin, "furniture");
        if (!armorStand.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String id = armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        Optional<Furniture> optionalFurniture = plugin.getFurnitureUtils().getFurniture(id);
        if (optionalFurniture.isEmpty()) return;

        Furniture furniture = optionalFurniture.get();
        FurnitureInteractEvent interactEvent = new FurnitureInteractEvent(player, armorStand, furniture);
        Bukkit.getServer().getPluginManager().callEvent(interactEvent);
    }
}
