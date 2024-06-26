package fr.olten.xmas.listener.player;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;


/**
 * @author Azodox_ (Luke)
 * 27/5/2022.
 */

public class PlayerMoveListener implements Listener {

    private final Lobby lobby;
    private final @NotNull Location teleportationLocation;
    private final @NotNull Location villageLocation;
    private final @NotNull Location teleportationToSurvival;
    private final @NotNull String survivalServerName;

    public PlayerMoveListener(Lobby lobby) {
        this.lobby = lobby;
        var locationString = lobby.getConfig().getString("portalToVillageCenter");
        var villageLocationString = lobby.getConfig().getString("villageLocation");
        var teleportationToSurvivalString = lobby.getConfig().getString("teleportToSurvivalLocation");
        var survivalServerName = lobby.getConfig().getString("serversName.survival");

        Preconditions.checkNotNull(locationString, "portalToVillageCenter is not set in config.yml");
        Preconditions.checkNotNull(villageLocationString, "villageLocation is not set in config.yml");
        Preconditions.checkNotNull(teleportationToSurvivalString, "teleportToSurvivalLocation is not set in config.yml");
        Preconditions.checkNotNull(survivalServerName, "serversName.survival is not set in config.yml");
        this.teleportationLocation = LocationSerialization.deserialize(locationString);
        this.villageLocation = LocationSerialization.deserialize(villageLocationString);
        this.teleportationToSurvival = LocationSerialization.deserialize(teleportationToSurvivalString);
        this.survivalServerName = survivalServerName;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        var player = event.getPlayer();
        var under = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if(under.equals(Material.OAK_SLAB) || under.equals(Material.OAK_PLANKS) || under.equals(Material.BEDROCK)){
            teleportationLocation.getNearbyLivingEntities(0.5).stream().filter(e -> e.getType().equals(EntityType.PLAYER)).map(e -> (Player) e).forEach(nearPlayer -> {
                nearPlayer.teleport(villageLocation);
            });
        }else {
            teleportationToSurvival.getNearbyLivingEntities(2.0, 0.5, 2.0).stream().filter(e -> e.getType().equals(EntityType.PLAYER)).map(e -> (Player) e).forEach(nearPlayer -> {
                var out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(survivalServerName);
                nearPlayer.sendPluginMessage(lobby, "BungeeCord", out.toByteArray());
            });
        }
    }
}
