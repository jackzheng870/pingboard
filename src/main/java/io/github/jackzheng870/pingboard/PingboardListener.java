package io.github.jackzheng870.pingboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PingboardListener implements Listener {
    private PingboardPlugin plugin;
    private PingboardCore core;

    public PingboardListener(PingboardPlugin plugin, PingboardCore core) {
        this.plugin = plugin;
        this.core = core;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                core.addEntry(player);
                keepDisplaying(player);
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        core.removeEntry(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        keepDisplaying(event.getPlayer());
    }

    private void keepDisplaying(Player player) {
        if (plugin.getConfig().getBoolean(player.getName())) {
            core.addPlayer(player);
        }
    }
}
