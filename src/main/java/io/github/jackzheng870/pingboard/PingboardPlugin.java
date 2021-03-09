package io.github.jackzheng870.pingboard;

import org.bukkit.plugin.java.JavaPlugin;

public class PingboardPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("pingboard").setExecutor(new PingboardCommand());
        getLogger().info("Pingboard has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Pingboard has been disabled.");
    }
}
