package fr.olten.xmas.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScrollerInventory {

    private int currentPage = 0;

    private final UUID id;

    private final List<Inventory> pages = new ArrayList<>();
    private static final HashMap<UUID, ScrollerInventory> users = new HashMap<>();

    private static final String nextPageName = ChatColor.RED + "> Page Suivante";
    private static final String previousPageName = ChatColor.RED + "< Page Précédente";

    public ScrollerInventory(List<ItemStack> items, Component component, Player player){
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(component);

        for (ItemStack item : items) {
            if (page.firstEmpty() >= 36) {
                pages.add(page);

                int index = pages.indexOf(page);
                page = pages.get(index);

                page.setItem(44, getNextPage());

                if (index > 0) {
                    page.setItem(36, getPreviousPage());
                }

                page.setItem(40, getItemPageInfo(index + 1));
                pages.set(index, page);

                page = getBlankPage(component);
                page.addItem(item);

                page.setItem(36, getPreviousPage());
                page.setItem(40, getItemPageInfo(index + 2));
            } else {
                int index = pages.indexOf(page);
                page.setItem(40, getItemPageInfo(index));

                if (pages.size() > 0) {
                    page.setItem(36, getPreviousPage());
                }

                page.addItem(item);
            }
        }
        pages.add(page);
        player.openInventory(pages.get(currentPage));
        users.put(player.getUniqueId(), this);
    }

    private Inventory getBlankPage(Component name){
        return Bukkit.createInventory(null, 5 * 9, name);
    }

    private ItemStack getNextPage(){
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageM = nextPage.getItemMeta();
        nextPageM.setDisplayName(nextPageName);
        nextPage.setItemMeta(nextPageM);
        return nextPage;
    }

    private ItemStack getPreviousPage(){
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousPageM = previousPage.getItemMeta();
        previousPageM.setDisplayName(previousPageName);
        previousPage.setItemMeta(previousPageM);
        return previousPage;
    }

    private ItemStack getItemPageInfo(int currentPage){
        ItemStack information = new ItemStack(Material.PAPER);
        ItemMeta informationM = information.getItemMeta();
        String currPage = currentPage == -1 ? "Dernière" : String.valueOf(currentPage);
        informationM.setDisplayName("§7Page actuelle : §c" + currPage);
        information.setItemMeta(informationM);
        return information;
    }

    public static String getNextPageName() {
        return nextPageName;
    }

    public static String getPreviousPageName() {
        return previousPageName;
    }

    public List<Inventory> getPages() {
        return pages;
    }

    public static HashMap<UUID, ScrollerInventory> getUsers() {
        return users;
    }

    public UUID getId() {
        return id;
    }

    public static class ScrollerInventoryListener implements Listener {

        @EventHandler
        public void onClick(InventoryClickEvent e){
            Player player = (Player) e.getWhoClicked();

            if(e.getCurrentItem() == null) return;
            ItemStack item = e.getCurrentItem();

            if(!item.hasItemMeta()) return;
            if(!item.getItemMeta().hasDisplayName()) return;
            if(!ScrollerInventory.getUsers().containsKey(player.getUniqueId())) return;
            ScrollerInventory inventory = ScrollerInventory.getUsers().get(player.getUniqueId());

            String itemName = item.getItemMeta().getDisplayName();
            if(itemName.equals(ScrollerInventory.getNextPageName())){
                if(inventory.currentPage >= inventory.getPages().size() - 1){
                    return;
                }

                inventory.currentPage += 1;
                player.openInventory(inventory.getPages().get(inventory.currentPage));

            }else if(itemName.equals(ScrollerInventory.getPreviousPageName())){
                if(inventory.currentPage > 0){
                    inventory.currentPage -= 1;
                    player.openInventory(inventory.getPages().get(inventory.currentPage));
                }else if(inventory.currentPage == -1){
                    inventory.currentPage = inventory.getPages().size() - 2;
                    player.openInventory(inventory.getPages().get(inventory.currentPage));
                }
            }
            e.setCancelled(true);
        }
    }
}