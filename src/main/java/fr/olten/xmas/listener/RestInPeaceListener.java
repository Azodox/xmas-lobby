package fr.olten.xmas.listener;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.LocationSerialization;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * This class will teleport a player that interacts with sign that is programmed to teleport him to a location.
 * In some cases, the player will be teleported to the spawn location.
 * It's linked with what we call the "RIP board" in the game and this explains the file name.
 *
 * @author Azodox_ (Luke)
 */
public class RestInPeaceListener implements Listener {

    private final Lobby lobby;

    public RestInPeaceListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction().isRightClick()) {
            if (event.getClickedBlock() != null) {
                if(event.getClickedBlock().getState() instanceof Sign sign){
                    var loc = sign.getLocation().clone();
                    loc.setYaw(0f);
                    loc.setPitch(0f);
                    assert lobby.getConfig().getConfigurationSection("signs") != null;
                    lobby.getConfig().getConfigurationSection("signs").getKeys(false).forEach(key -> {
                        assert lobby.getConfig().getString("signs." + key) != null;
                        if (LocationSerialization.deserialize(lobby.getConfig().getString("signs." + key)).equals(loc)) {
                            event.getPlayer().teleport(LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("spawn"))));
                        }
                    });
                }
            }
        }
    }
}
