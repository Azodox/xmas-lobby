package fr.olten.xmas.listener.sign;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.roulette.keys.KeyDAL;
import fr.olten.xmas.roulette.keys.KeyManager;
import fr.olten.xmas.roulette.keys.KeyTypes;
import fr.olten.xmas.utils.ScrollerInventory;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SignInteractListener implements Listener {

    private final Lobby lobby;

    public SignInteractListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() == null) return;
        if(event.getAction().isRightClick()){
            if(event.getClickedBlock().getState() instanceof Sign sign){
                if(sign.getLocation().equals(lobby.getCarousel().getJoiningSign().getLocation())){
                    lobby.getCarousel().manager().ride(event.getPlayer());
                }

                if(sign.getLocation().equals(lobby.getRoulette().getStartSign().getLocation())){
                    openRouletteMenu(event.getPlayer());
                }
            }
        }
    }

    // Open the roulette inventory to the player.
    private void openRouletteMenu(Player player){

        var items = new ArrayList<ItemStack>();

        KeyDAL keyDAL = new KeyDAL(lobby);
        Document doc = keyDAL.getKeys(player);
        KeyManager keyManager = new KeyManager(lobby);

        for (KeyTypes key : KeyTypes.values()) {
            int i = doc.getInteger(key.name().toLowerCase());
            if(i > 0){
                ItemStack stack = keyManager.getKey(key).getItem();
                stack.setAmount(i);
                items.add(stack);
            }
        }

        new ScrollerInventory(items, Component.text("Roulette"), player);
    }
}
