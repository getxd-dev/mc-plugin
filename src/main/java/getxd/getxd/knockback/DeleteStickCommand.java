package getxd.getxd.knockback;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
public class DeleteStickCommand implements CommandExecutor {

    private Knockback plugin;

    public DeleteStickCommand(Knockback plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("knockbackstick.delete")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getInventory();
                for (ItemStack item : inventory.getContents()) {
                    if (item != null && item.getType() == Material.STICK && item.getEnchantmentLevel(Enchantment.KNOCKBACK) == plugin.getKnockbackLevel()) {
                        inventory.remove(item);
                    }
                }
            }
            sender.sendMessage("All the super knockback sticks have been deleted!");
        }
        return true;
    }
}
