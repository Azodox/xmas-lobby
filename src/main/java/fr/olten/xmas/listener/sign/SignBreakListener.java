package fr.olten.xmas.listener.sign;

import fr.olten.xmas.Lobby;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreakListener implements Listener {

    private final Lobby lobby;
    public SignBreakListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getState() instanceof Sign sign){
            if(sign.getLocation().equals(lobby.getCarousel().getJoiningSign().getLocation())){
                event.setCancelled(true);
            }
        }
    }
}
