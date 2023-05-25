package getxd.getxd.knockback;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Knockback extends JavaPlugin implements Listener {

    private int knockbackLevel;
    private double enchantChance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        knockbackLevel = getConfig().getInt("knockback-level");
        enchantChance = getConfig().getDouble("enchant-chance");

        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("deletestick").setExecutor(new DeleteStickCommand(this));
        this.getCommand("reload").setExecutor(new ReloadCommand(this));
    }
    public int getKnockbackLevel() {
        return knockbackLevel;
    }

    public void setKnockbackLevel(int knockbackLevel) {
        this.knockbackLevel = knockbackLevel;
    }

    public void setEnchantChance(double enchantChance) {
        this.enchantChance = enchantChance;
    }

    public ItemStack getStick(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.STICK) {
                return item;
            }
        }
        // If no stick was found, return null
        return null;
    }

    public void addKnockback(ItemStack item) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            item = new ItemStack(Material.STICK, 1);
        }
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, knockbackLevel);
        // set lore :flushed: to display dura SMP LORE REAL
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Durability: 5/5");
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // get player moving thing
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack stick = getStick(inventory);
        if (stick != null) {
            // check if the stick already has enchantment xdd
            if (stick.getEnchantmentLevel(Enchantment.KNOCKBACK) != knockbackLevel) {
                double random = Math.random();
                double percentage = random * 100;
                if (percentage <= enchantChance) {
                    addKnockback(stick);
                    String goldText = ChatColor.GOLD.toString();
                    player.sendMessage(goldText + "[Test Plugin] You have a super knockback stick! Have fun! (" + (enchantChance) + "% Chance!).");
                    Logger logger = this.getLogger();
                    // Log a message to the console with the name of the player who received the knockback stick
                    logger.info(player.getName() + " has received a super knockback stick!");
                }
            }
        }
    }

//dura system
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() == Material.STICK && item.getEnchantmentLevel(Enchantment.KNOCKBACK) == knockbackLevel) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                String durabilityString = lore.get(0);
                int durability = Integer.parseInt(durabilityString.split("/")[0].split(": ")[1]);
                durability--;
                if (durability == 0) {
                    player.getInventory().remove(item);
                    player.sendMessage(ChatColor.RED + "[Test Plugin] Your super knockback stick has broken!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                } else {
                    lore.set(0, "Durability: " + durability + "/5");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            }
        }
    }
}
