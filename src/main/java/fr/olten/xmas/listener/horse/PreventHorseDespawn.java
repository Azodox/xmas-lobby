package fr.olten.xmas.listener.horse;

import fr.olten.xmas.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class PreventHorseDespawn implements Listener {

    private final Lobby lobby;
    public PreventHorseDespawn(Lobby lobby) {
        this.lobby = lobby;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (lobby.getCarousel().getCenter().getChunk().equals(event.getChunk())) {
            lobby.getCarousel().despawn();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent event) {
        if (lobby.getCarousel().getCenter().getWorld().equals(event.getWorld())) {
            lobby.getCarousel().despawn();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (lobby.getCarousel().getCenter().getChunk().equals(event.getChunk())) {
            lobby.getCarousel().spawn();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldLoadEvent event){
        if (lobby.getCarousel().getCenter().getWorld().equals(event.getWorld())) {
            lobby.getCarousel().spawn();
        }
    }
}
