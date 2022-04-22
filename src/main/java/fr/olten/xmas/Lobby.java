package fr.olten.xmas;

import fr.olten.xmas.achievement.Achievement;
import fr.olten.xmas.carousel.Carousel;
import fr.olten.xmas.carousel.Engine;
import fr.olten.xmas.listener.PlayerJoinListener;
import fr.olten.xmas.listener.PlayerQuitListener;
import fr.olten.xmas.listener.ProtectionListener;
import fr.olten.xmas.listener.horse.HorseDismountListener;
import fr.olten.xmas.listener.horse.HorseGotDamagedListener;
import fr.olten.xmas.listener.horse.HorseMountListener;
import fr.olten.xmas.listener.rank.RankChangedListener;
import fr.olten.xmas.listener.sign.SignBreakListener;
import fr.olten.xmas.listener.sign.SignInteractListener;
import fr.olten.xmas.manager.TeamNameTagManager;
import fr.olten.xmas.rewards.RewardHandler;
import fr.olten.xmas.roulette.Roulette;
import net.valneas.account.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Lobby extends JavaPlugin {

    private final Set<Achievement> achievements = new HashSet<>();
    private Carousel carousel;
    private Roulette roulette;

    @Override
    public void onEnable() {
        var DEFAULT_WORLD = Bukkit.getWorld("world");
        this.carousel = new Carousel(new Location(DEFAULT_WORLD, -483.0, 102.0, -461.0),
                new Location(DEFAULT_WORLD, -494.0, 100.0, -461, (float) -90.0, (float) 0.0),
                (Sign) DEFAULT_WORLD.getBlockAt(new Location(DEFAULT_WORLD, -492.0, 100.0, -461.0)).getState());

        this.roulette = new Roulette((Sign) DEFAULT_WORLD.getBlockAt(new Location(DEFAULT_WORLD, -560.0, 102.0, -424.0)).getState(),
                                Arrays.asList(new Location(DEFAULT_WORLD, -558.0, 104, -423),new Location(DEFAULT_WORLD, -559.0, 104.0, -423.0),
                                             new Location(DEFAULT_WORLD, -560, 104.0, -423.0),new Location(DEFAULT_WORLD, -561.0, 104.0, -423.0),
                                             new Location(DEFAULT_WORLD, -562.0, 104.0, -423.0),new Location(DEFAULT_WORLD, -562.0, 103.0, -423.0),
                                             new Location(DEFAULT_WORLD, -562.0, 102.0, -423.0),new Location(DEFAULT_WORLD, -562.0, 101.0, -423.0),
                                             new Location(DEFAULT_WORLD, -562.0, 100.0, -423.0), new Location(DEFAULT_WORLD, -561.0, 100.0, -423.0),
                                             new Location(DEFAULT_WORLD, -560.0, 100.0, -423.0), new Location(DEFAULT_WORLD, -559.0, 100.0, -423.0),
                                             new Location(DEFAULT_WORLD, -558.0, 100.0, -423.0),new Location(DEFAULT_WORLD, -558.0, 101.0, -423.0),
                                             new Location(DEFAULT_WORLD, -558.0, 102.0, -423.0),new Location(DEFAULT_WORLD, -558.0, 103.0, -423.0),
                                             new Location(DEFAULT_WORLD, -558.0, 104.0, -423.0)),this);

        new Engine(this).runTaskTimer(this, 0L, 1L);

        Arrays.stream(RankUnit.values()).forEach(TeamNameTagManager::init);
        getServer().getOnlinePlayers().forEach(TeamNameTagManager::update);

        this.registerAchievements();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseMountListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseGotDamagedListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseDismountListener(this), this);
        getServer().getPluginManager().registerEvents(new SignInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SignBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new RankChangedListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        TeamNameTagManager.reset();
        carousel.despawn();
        getLogger().info("Disabled!");
    }

    private void registerAchievements() {
        this.achievements.forEach(achievement -> this.getServer().getPluginManager().registerEvents(achievement, this));
    }

    public Carousel getCarousel() {
        return carousel;
    }
    public Roulette getRoulette() {
        return roulette;
    }
}
