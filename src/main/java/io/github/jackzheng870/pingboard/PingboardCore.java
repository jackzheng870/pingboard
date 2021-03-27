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
    private Scoreboard pingboard;
    private Objective objective;
    private HashMap<String, BukkitTask> updatePingTasks = new HashMap<>();

    public PingboardCore(PingboardPlugin plugin) {
        this.plugin = plugin;
        newPingboard();
    }

    void addEntry(Player player) {
        String playerName = player.getName();

        if (updatePingTasks.get(playerName) != null) {
            updatePingTasks.remove(playerName).cancel();
        }
        updatePing(objective.getScore(playerName), player);
    }

    void removeEntry(Player player) {
        String playerName = player.getName();

        updatePingTasks.remove(playerName).cancel();
        pingboard.resetScores(playerName);
    }

    void addPlayer(Player player) {
        player.setScoreboard(pingboard);
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
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("pingboard", "dummy", "Pingboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.pingboard = scoreboard;
        this.objective = objective;
    }

    private void refreshForPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            addEntry(player);
            keepDisplaying(player);
        });
    }

    private void updatePing(Score score, Player player) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                score.setScore(getPing(player));
            }
        }.runTaskTimer(plugin, 0, 20);

        updatePingTasks.put(player.getName(), task);
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
