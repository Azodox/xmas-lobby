package fr.olten.xmas.manager;

import fr.mrmicky.fastparticles.ParticleType;
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
        if(!player.hasPlayedBefore()){
            var spawn = LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("spawn")));
            player.teleport(spawn);
        }
    }

    public void joiningPlayerFromSurvival(Player player){
        var magic = ParticleType.of("SPELL_WITCH");
        var survivalPortalSpawnLocation = LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("survivalSpawnPortal")));

        var particleLocation = survivalPortalSpawnLocation.clone();
        /*
            Credits to Finnbon (https://linktr.ee/finnbon)
         */
        var task = lobby.getServer().getScheduler().runTaskTimer(lobby, () -> {
            for (double i = 0; i <= Math.PI; i += Math.PI / 15) { // 10 being the amount of circles.
                double radius = Math.sin(i); // we get the current radius
                double y = Math.cos(i); // we get the current y value.
                for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                    double x = Math.cos(a) * radius; // x-coordinate
                    double z = Math.sin(a) * radius; // z-coordinate
                    particleLocation.add(x, y /* from the previous code */, z); // 'location' is our center.
                    magic.spawn(particleLocation.getWorld(), particleLocation, 1, 0, 0, 0, 0);
                    particleLocation.subtract(x, y, z);
                }
            }
        }, 0, 1);

        lobby.getServer().getScheduler().runTaskLater(lobby, task::cancel, 20 * 2);
        player.teleport(survivalPortalSpawnLocation);
    }
}
