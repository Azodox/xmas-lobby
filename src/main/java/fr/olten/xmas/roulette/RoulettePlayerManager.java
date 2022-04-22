package fr.olten.xmas.roulette;

import fr.olten.xmas.carousel.Carousel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RoulettePlayerManager {

    private final Roulette roulette;

    public RoulettePlayerManager(Roulette roulette) {
        this.roulette = roulette;
    }

    // Set the current turning player.
    public void setPlayer(Player player) {
        if(roulette.currentPlayer() != null) {
            Bukkit.getLogger().severe("[Roulette] Can't define player because someone is already turning the roulette !");
            player.sendMessage(Component.text("[Roulette] Quelqu'un est déjà entrain de tourner la roue !").color(TextColor.color(255, 0, 0)));
        }
        else if(roulette.isRunning()) {
            Bukkit.getLogger().severe("[Roulette] Can't define player because someone is already turning the roulette !");
            player.sendMessage(Component.text("[Roulette] Quelqu'un est déjà entrain de tourner la roue !").color(TextColor.color(255, 0, 0)));
        }
        else{
            roulette.currentPlayer(player);
        }
    }

    // Remove the player turning the roulette.
    public void removePlayer() {
        if(roulette.currentPlayer() == null) {
            Bukkit.getLogger().severe("[Roulette] Can't remove player because nobody is turning the roulette !");
            return;
        }
        roulette.currentPlayer(null);
    }

    public void stop() {
        roulette.stop();
    }

    public void start() {
        if(roulette.currentPlayer() == null) {
            Bukkit.getLogger().severe("[Roulette] Can't start roulette because nobody is turning the roulette !");
        }
        else if(roulette.isRunning()) {
            Bukkit.getLogger().severe("[Roulette] Can't start roulette because roulette is already running !");
        }
        else{
            roulette.start();
            roulette.currentPlayer().sendMessage(Component.text("[Roulette] La roulette a démarré !").color(TextColor.color(0, 255, 0)));
        }
    }

}
