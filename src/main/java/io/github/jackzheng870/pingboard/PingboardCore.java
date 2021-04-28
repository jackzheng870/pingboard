package io.github.jackzheng870.pingboard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PingboardCore {
    private PingboardPlugin plugin;
    private Scoreboard scoreboard;
    private Objective objective;
    private HashMap<String, BukkitTask> updatePingTasks = new HashMap<>();

    public PingboardCore(PingboardPlugin plugin) {
        this.plugin = plugin;
        newPingboard();
    }

    void addEntry(Player player) {
        String playerName = player.getName();
        Score score = objective.getScore(playerName);

        BukkitTask newTask = new BukkitRunnable() {
            @Override
            public void run() {
                score.setScore(getPing(player));
            }
        }.runTaskTimer(plugin, 0, 20);

        BukkitTask oldTask = updatePingTasks.put(playerName, newTask);
        if (oldTask != null) {
            oldTask.cancel();
        }
    }

    void removeEntry(Player player) {
        String playerName = player.getName();

        updatePingTasks.remove(playerName).cancel();
        scoreboard.resetScores(playerName);
    }

    void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    void removePlayer(Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        newPingboard();
        refreshForPlayers();
    }

    void keepDisplaying(Player player) {
        if (plugin.getConfig().getBoolean(player.getName())) {
            addPlayer(player);
        }        
    }

    private void newPingboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("pingboard", "dummy", "Pingboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void refreshForPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            addEntry(player);
            keepDisplaying(player);
        });
    }

    private int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
