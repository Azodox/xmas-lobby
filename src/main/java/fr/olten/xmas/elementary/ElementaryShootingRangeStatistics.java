package fr.olten.xmas.elementary;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Azodox_ (Luke)
 * 22/5/2022.
 */

public class ElementaryShootingRangeStatistics {

    private final ElementaryShootingRange shootingRange;

    private final Map<UUID, Integer> killedStands = new HashMap<>();
    private final Map<UUID, Duration> upTimes = new HashMap<>();

    private int bestScore;

    private final File playersStatisticsFolder;

    public ElementaryShootingRangeStatistics(ElementaryShootingRange shootingRange) {
        this.shootingRange = shootingRange;
        this.playersStatisticsFolder = new File(shootingRange.getFolder(), "players");
        if (!playersStatisticsFolder.exists())
            playersStatisticsFolder.mkdirs();
    }

    public void addKilledStand(Player player) {
        killedStands.put(player.getUniqueId(), getKilledStands(player.getUniqueId()) + 1);
    }

    public int getKilledStands(UUID uuid) {
        return killedStands.getOrDefault(uuid, 0);
    }

    public void addUptime(Player player, Duration duration) {
        upTimes.put(player.getUniqueId(), getUptime(player.getUniqueId()).plus(duration));
    }

    public Duration getUptime(UUID uuid) {
        return upTimes.getOrDefault(uuid, Duration.ZERO);
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public void saveStatistic(UUID uuid) throws IOException {
        Preconditions.checkNotNull(shootingRange.getGame(), "There is no game started!");

        var playerFolder = new File(playersStatisticsFolder, uuid.toString());
        if(!playerFolder.exists())
            playerFolder.mkdirs();

        var now = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        var gameFile = new File(playerFolder, shootingRange.getGame().getId() + "-" + now + ".yml");
        if(!gameFile.exists())
            gameFile.createNewFile();

        var statisticsFile = YamlConfiguration.loadConfiguration(gameFile);
        statisticsFile.set("gameId", shootingRange.getGame().getId());
        statisticsFile.set("date", now);

        killedStands.forEach((id, kills) -> {
            if(id.equals(uuid)){
                statisticsFile.set("statistics.kills", kills);
            }
        });

        upTimes.forEach((id, uptime) -> {
            if(id.equals(uuid)){
                statisticsFile.set("statistics.timeSpentInSec", uptime.getSeconds());
            }
        });

        statisticsFile.set("statistics.bestScore", bestScore);

        statisticsFile.save(gameFile);
    }
}
