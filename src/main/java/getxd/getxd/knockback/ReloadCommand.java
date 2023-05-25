package getxd.getxd.knockback;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private Knockback plugin;

    public ReloadCommand(Knockback plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("knockbackstick.reload")) {
            // Reload the config.yml file
            plugin.reloadConfig();
            // Load the configuration options from the config.yml file
            plugin.setKnockbackLevel(plugin.getConfig().getInt("knockback-level"));
            plugin.setEnchantChance(plugin.getConfig().getDouble("enchant-chance"));
            sender.sendMessage("The configuration file has been reloaded!");
        }
        return true;
    }
}

