package fr.olten.xmas.manager;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.LocationSerialization;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerManager {

    private final Lobby lobby;

    public PlayerManager(Lobby lobby) {
        this.lobby = lobby;
    }

    public void joiningPlayer(Player player){
        var spawn = LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("spawn")));
        player.teleport(spawn);
    }
}
