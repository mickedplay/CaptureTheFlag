package play.mickedplay.ctf.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import play.mickedplay.ctf.CaptureTheFlag;
import play.mickedplay.ctf.player.CTFPlayer;
import play.mickedplay.ctf.shop.ShopItem;
import play.mickedplay.gameapi.utilities.builder.ItemBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by mickedplay on 30.04.2016 at 18:10 UTC+1.
 * You are not allowed to remove this comment.
 */
public class ItemShop implements Listener {

    private CaptureTheFlag ctf;
    private ItemStack shopOpenInventoryItem;
    private Inventory inventory;
    private HashMap<ItemStack, ShopItem> shopItems;

    public ItemShop(CaptureTheFlag ctf) {
        this.ctf = ctf;
        this.shopOpenInventoryItem = new ItemBuilder(Material.ENDER_CHEST).withName("§7Shop").build();
        this.inventory = Bukkit.createInventory(null, 27, "§8Shop");
        this.shopItems = new HashMap<>();
        this.initializeShopItems();
        Bukkit.getPluginManager().registerEvents(this, ctf.getPlugin());
    }

    private void initializeShopItems() {
        ItemStack emptySlot = new ItemBuilder(Material.STAINED_GLASS_PANE).withDamage((short) 15).withName(" ").build();
        this.inventory.setItem(0, new ItemBuilder(Material.IRON_INGOT).withName("§6Für jeden erhältlich").build());
        this.inventory.setItem(9, new ItemBuilder(Material.DIAMOND).withName("§cBenötigter Highscore: §e500.000").build());
        this.inventory.setItem(18, new ItemBuilder(Material.EMERALD).withName("§cBenötigter Highscore: §e2.500.000").build());
        new ShopItem(this, 3, Material.WOOD_PLATE, "Land mine", Collections.singletonList("Explodiert, wenn ein Gegner rüberläuft"));
        new ShopItem(this, 1, Material.COAL, "Grenade (Blindness)", Collections.singletonList("Eine Rauchwolke mit einem Durchmesser von " + GameSettings.EXPLOSION_RADIUS + " Blöcken erscheint"));
        new ShopItem(this, 12, Material.WOOD_HOE, "Railgun", Collections.singletonList("Entfacht einen Strahl, der Gegner sofort tötet"));
        new ShopItem(this, 5, Material.CAKE, "Healing Pad", Collections.singletonList("Heilt alle Teammitglieder in einem " + GameSettings.CAKE_HEAL_RADIUS + "-Block-Radius"));
        new ShopItem(this, 9, Material.BLAZE_POWDER, "Enemy Slayer", Collections.singletonList("Tötet einen zufälligen Gegner."));
        new ShopItem(this, 4, Material.SLIME_BLOCK, "Energy Drink", Collections.singletonList("Boostet dich nach vorne"));
//        new ShopItem(this, 85, Material.BARRIER, "Shield", Arrays.asList("Protects your flag for 25 seconds"));

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, emptySlot);
            }
        }
    }

    public HashMap<ItemStack, ShopItem> getShopItems() {
        return shopItems;
    }

    public ItemStack getShopOpenInventoryItem() {
        return shopOpenInventoryItem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inventory = e.getClickedInventory();
            if (inventory.getName().equals(this.inventory.getName())) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                        CTFPlayer ctfPlayer = this.ctf.getCTFPlayer((Player) e.getWhoClicked());
                        if (this.shopItems.containsKey(e.getCurrentItem())) {
                            int costs = this.shopItems.get(e.getCurrentItem()).getCosts();
                            if (ctfPlayer.getTeam().getExperience() >= costs) {
                                if (ctfPlayer.hasItem(e.getCurrentItem())) {
                                    ctfPlayer.sendMessage("§cDu hast bereits dieses Item!");
                                    ctfPlayer.playSound(Sound.ENTITY_VILLAGER_NO);
                                } else {
                                    ctfPlayer.getTeam().removeExperience(costs);
                                    ctfPlayer.addItem(e.getCurrentItem());
                                    ctfPlayer.addClaimedShopItem(e.getCurrentItem());
                                    ctfPlayer.getGameStats().addBoughtTeamItem();
                                    ctfPlayer.playSound(Sound.ENTITY_VILLAGER_YES);
                                    ctfPlayer.closeInventory();
                                }
                            } else {
                                ctfPlayer.sendMessage("§cDein Team hat nicht genügend Erfahrung!!");
                                ctfPlayer.playSound(Sound.ENTITY_VILLAGER_NO);
                            }
                        }
                    }
                }
            }
        }
    }
}