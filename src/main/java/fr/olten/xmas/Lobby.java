package fr.olten.xmas;

import fr.olten.xmas.carousel.Carousel;
import fr.olten.xmas.carousel.Engine;
import fr.olten.xmas.listener.ProtectionListener;
import fr.olten.xmas.listener.horse.HorseDismountListener;
import fr.olten.xmas.listener.horse.HorseMountListener;
import fr.olten.xmas.listener.rank.RankChangedListener;
import fr.olten.xmas.listener.sign.SignBreakListener;
import fr.olten.xmas.listener.sign.SignInteractListener;
import fr.olten.xmas.listener.horse.HorseGotDamagedListener;
import fr.olten.xmas.listener.PlayerJoinListener;
import fr.olten.xmas.listener.PlayerQuitListener;
import fr.olten.xmas.manager.TeamNameTagManager;
import net.valneas.account.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Lobby extends JavaPlugin {

    private Carousel carousel;

    @Override
    public void onEnable() {
        var DEFAULT_WORLD = Bukkit.getWorld("world");
        this.carousel = new Carousel(new Location(DEFAULT_WORLD, -483.0, 102.0, -461.0),
                new Location(DEFAULT_WORLD, -494.0, 100.0, -461, (float) -90.0, (float) 0.0),
                (Sign) DEFAULT_WORLD.getBlockAt(new Location(DEFAULT_WORLD, -492.0, 100.0, -461.0)).getState());

        new Engine(this).runTaskTimer(this, 0L, 1L);

        Arrays.stream(RankUnit.values()).forEach(TeamNameTagManager::init);
        getServer().getOnlinePlayers().forEach(TeamNameTagManager::update);

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

    public Carousel getCarousel() {
        return carousel;
    }
}
