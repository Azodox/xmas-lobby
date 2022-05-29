package fr.olten.xmas.listener.projectile;

import fr.olten.xmas.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;


/**
 * @author Azodox_ (Luke)
 * 22/5/2022.
 */

public class ProjectileLaunchedListener implements Listener {
    private final Lobby lobby;

    public ProjectileLaunchedListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onProjectileLaunched(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow arrow) {
            if (arrow.getShooter() instanceof Player player) {
                if (lobby.getShootingRange().getPlayers().contains(player)) {
                    Bukkit.getScheduler().runTaskLater(lobby, arrow::remove, 40);
                }
            }
        }
    }
}
