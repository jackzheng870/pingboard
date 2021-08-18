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
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("pingboard", "dummy", "Pingboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    void addEntry(Player player) {
        String playerName = player.getName();
        Score score = objective.getScore(playerName);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                score.setScore(getPing(player));
            }
        }.runTaskTimer(plugin, 0, 20);

        updatePingTasks.put(playerName, task);
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
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    private int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
        } catch (NoSuchFieldException e) {
            return player.getPing();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
