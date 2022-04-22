package fr.olten.xmas.listener.sign;

import fr.olten.xmas.Lobby;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreakListener implements Listener {

    private final Lobby lobby;
    public SignBreakListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getState() instanceof Sign sign){
            if(sign.getBlock().getLocation().equals(lobby.getCarousel().getJoiningSign().getLocation())){
                event.setCancelled(true);
            }
            if(sign.getBlock().getLocation().equals(lobby.getRoulette().getStartSign().getLocation())){
                event.setCancelled(true);
            }
        }
    }
}
