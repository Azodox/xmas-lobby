package fr.olten.xmas.carousel;

import fr.olten.xmas.Lobby;
import org.bukkit.scheduler.BukkitRunnable;

public class Engine extends BukkitRunnable {

    private final Lobby lobby;

    public Engine(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        this.lobby.getCarousel().update();
    }
}
