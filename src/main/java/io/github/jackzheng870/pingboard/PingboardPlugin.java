package io.github.jackzheng870.pingboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PingboardPlugin extends JavaPlugin {
    Scoreboard pingboard;
    private List<BukkitTask> updatePingTasks = new ArrayList<BukkitTask>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PingboardListener(this), this);
        getCommand("pingboard").setExecutor(new PingboardCommand(this));
        getLogger().info("Pingboard has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Pingboard has been disabled.");
    }

    Scoreboard getBoard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("pingboard", "dummy", "Pingboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        updatePingTasks.forEach(task -> task.cancel());
        updatePingTasks.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Score score = objective.getScore(player.getName());

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    score.setScore(getPing(player));
                }
            }.runTaskTimer(this, 0, 20);

            updatePingTasks.add(task);
        }
        return scoreboard;
    }

    private int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
