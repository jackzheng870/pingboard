package io.github.jackzheng870.pingboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PingboardCommand implements CommandExecutor {
    private PingboardPlugin plugin;
    private PingboardCore core;

    public PingboardCommand(PingboardPlugin plugin, PingboardCore core) {
        this.plugin = plugin;
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This commmand can only be used by the player.");
            return true;
        }

        togglePingboard((Player) sender);
        return true;
    }

    private void togglePingboard(Player player) {
        FileConfiguration config = plugin.getConfig();
        String playerName = player.getName();

        if (config.getBoolean(playerName)) {
            config.set(playerName, false);
            core.removePlayer(player);
        } else {
            config.set(playerName, true);
            core.addPlayer(player);
        }

        plugin.saveConfig();
    }
}
