package io.github.jackzheng870.pingboard;

import org.bukkit.plugin.java.JavaPlugin;

public class PingboardPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        PingboardCore core = new PingboardCore(this);
        getServer().getPluginManager().registerEvents(new PingboardListener(this, core), this);
        getCommand("pingboard").setExecutor(new PingboardCommand(this, core));
        getLogger().info("Pingboard has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Pingboard has been disabled.");
    }
}
