package fr.olten.xmas;

import fr.olten.xmas.achievement.Achievement;
import fr.olten.xmas.carousel.Carousel;
import fr.olten.xmas.carousel.Engine;
import fr.olten.xmas.elementary.ElementaryShootingRange;
import fr.olten.xmas.listener.FallDamageListener;
import fr.olten.xmas.listener.IncomingPluginMessageListener;
import fr.olten.xmas.listener.ProtectionListener;
import fr.olten.xmas.listener.RestInPeaceListener;
import fr.olten.xmas.listener.entity.EntityDamageByEntityListener;
import fr.olten.xmas.listener.entity.EntityDeathListener;
import fr.olten.xmas.listener.entity.EntityMoveListener;
import fr.olten.xmas.listener.horse.HorseDismountListener;
import fr.olten.xmas.listener.horse.HorseGotDamagedListener;
import fr.olten.xmas.listener.horse.HorseMountListener;
import fr.olten.xmas.listener.player.PlayerCommandPreprocessListener;
import fr.olten.xmas.listener.player.PlayerJoinListener;
import fr.olten.xmas.listener.player.PlayerMoveListener;
import fr.olten.xmas.listener.player.PlayerQuitListener;
import fr.olten.xmas.listener.projectile.ProjectileLaunchedListener;
import fr.olten.xmas.listener.rank.RankChangedListener;
import fr.olten.xmas.listener.sign.SignBreakListener;
import fr.olten.xmas.listener.sign.SignInteractListener;
import fr.olten.xmas.manager.PlayerManager;
import fr.olten.xmas.manager.TeamNameTagManager;
import fr.olten.xmas.roulette.Roulette;
import fr.olten.xmas.task.SpawnPortalParticle;
import fr.olten.xmas.utils.Mongo;
import net.valneas.account.AccountSystem;
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
    private final PlayerManager playerManager = new PlayerManager(this);
    private Carousel carousel;
    private Roulette roulette;
    private Mongo mongo;
    private ElementaryShootingRange shootingRange;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mongo = new Mongo(this);

        this.getServer().getScheduler().runTaskTimer(this, new SpawnPortalParticle(this), 0, 1);

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

        this.shootingRange = new ElementaryShootingRange(this);
        new Engine(this).runTaskTimer(this, 0L, 1L);

        var provider = getServer().getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            provider.getProvider().getRankHandler().getAllRanksQuery().stream().map(rank -> (RankUnit) rank).forEach(TeamNameTagManager::init);
        }
        getServer().getOnlinePlayers().forEach(TeamNameTagManager::update);

        this.registerAchievements();

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "xmas:lobbysurvie", new IncomingPluginMessageListener(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "xmas:lobbysurvie");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseMountListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseGotDamagedListener(this), this);
        getServer().getPluginManager().registerEvents(new HorseDismountListener(this), this);
        getServer().getPluginManager().registerEvents(new SignInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new SignBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new RankChangedListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new RestInPeaceListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunchedListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        TeamNameTagManager.reset();
        carousel.despawn();
        shootingRange.pluginDisable();
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
    public Mongo getMongo() { return mongo; }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ElementaryShootingRange getShootingRange() {
        return shootingRange;
    }
}
