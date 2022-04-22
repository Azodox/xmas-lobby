package fr.olten.xmas.listener;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.roulette.keys.KeyTypes;
import fr.olten.xmas.utils.ScrollerInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RouletteEventHandler implements Listener {

    public Lobby main;

    public RouletteEventHandler(Lobby main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        var player = event.getWhoClicked();
        var item = event.getCurrentItem();
        var view = event.getView();

        if(!ScrollerInventory.getUsers().containsKey(player.getUniqueId())) return;

        if(view.title().contains(Component.text("Roulette"))) {
            if(!item.hasItemMeta()) return;
            if(!item.getItemMeta().hasDisplayName()) return;

            for(var key : KeyTypes.values()) {
                if(item.displayName().contains(Component.text(key.name()))) {
                    main.getRoulette().start(key);
                }
            }
        }
    }

}
