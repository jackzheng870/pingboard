package io.github.jackzheng870.pingboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class PingboardCommand implements CommandExecutor {
    private PingboardPlugin plugin;

    public PingboardCommand(PingboardPlugin pingboardPlugin) {
        plugin = pingboardPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This commmand can only be used by the player.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        if (plugin.getConfig().getBoolean(playerName)) {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            plugin.getConfig().set(playerName, false);
        } else {
            player.setScoreboard(plugin.pingboard);
            plugin.getConfig().set(playerName, true);
        }

        plugin.saveConfig();
        return true;
    }
}
