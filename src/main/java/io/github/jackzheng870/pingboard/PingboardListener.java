package io.github.jackzheng870.pingboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PingboardListener implements Listener {
    private PingboardPlugin plugin;

    public PingboardListener(PingboardPlugin pingboardPlugin) {
        plugin = pingboardPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                keepDisplaying(event.getPlayer());
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        keepDisplaying(event.getPlayer());
    }

    private void keepDisplaying(Player player) {
        if (plugin.getConfig().getBoolean(player.getName())) {
            player.setScoreboard(plugin.getBoard());
        }
    }
}
