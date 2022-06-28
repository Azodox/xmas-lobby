package fr.olten.xmas.listener;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;

public class ProtectionListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(!event.getPlayer().hasPermission("xmas.protect.break"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event){
        event.setCancelled(!event.getPlayer().hasPermission("xmas.protect.place"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBookTaken(PlayerTakeLecternBookEvent event){
        event.setCancelled(!event.getPlayer().hasPermission("xmas.protect.book"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemFrameChange(PlayerItemFrameChangeEvent event){
        event.setCancelled(!event.getPlayer().hasPermission("xmas.protect.itemframe"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArmorStandDamaged(EntityDamageByEntityEvent event){
        event.setCancelled(!event.getDamager().hasPermission("xmas.protect.armorstand"));
    }
}
