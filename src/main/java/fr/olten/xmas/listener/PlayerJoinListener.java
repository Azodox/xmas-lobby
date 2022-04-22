package fr.olten.xmas.listener;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.manager.TeamNameTagManager;
import fr.olten.xmas.roulette.keys.KeyDAL;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Lobby lobby;

    public PlayerJoinListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        KeyDAL keyDAL = new KeyDAL(lobby);
        keyDAL.initPlayerKeys(player);

        if(player.isInsideVehicle()) {
            if(lobby.getCarousel().isRide(player.getVehicle())) {
                lobby.getCarousel().manager().dismount(player);
            }
        }
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            var accountSystem = provider.getProvider();
            var accountManager = new AccountManager(accountSystem, player);
            if(!accountManager.hasAnAccount()){
                Bukkit.getScheduler().runTaskLater(lobby, () -> TeamNameTagManager.update(player), 5L);
            }else{
                TeamNameTagManager.update(player);
            }
        }
    }
}
