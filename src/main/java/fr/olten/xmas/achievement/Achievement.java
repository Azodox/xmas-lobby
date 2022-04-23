package fr.olten.xmas.achievement;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public interface Achievement extends Listener {

    Component getName();

    Component getDescription();

    double getEarnedPoints();

    void complete(UUID uuid);

    @EventHandler
    default void onJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    default void onQuit(PlayerQuitEvent event) {

    }

    @EventHandler
    default void onMove(PlayerMoveEvent event){

    }

    @EventHandler
    default void onInteract(PlayerInteractEvent event){

    }

    @EventHandler
    default void onInteractAtEntity(PlayerInteractAtEntityEvent event){

    }

    @EventHandler
    default void onClick(InventoryClickEvent event){

    }

    @EventHandler
    default void onDrop(PlayerDropItemEvent event){

    }

    @EventHandler
    default void onBlockBreak(BlockBreakEvent event){

    }

    @EventHandler
    default void onBlockPlace(BlockPlaceEvent event){

    }

    @EventHandler
    default void onBlockDamage(BlockDamageEvent event){

    }

    @EventHandler
    default void onDeath(PlayerDeathEvent event){

    }

    @EventHandler
    default void onRespawn(PlayerRespawnEvent event){

    }

    @EventHandler
    default void onFoodLevelChange(FoodLevelChangeEvent event){

    }

    @EventHandler
    default void onItemConsume(PlayerItemConsumeEvent event){

    }

    @EventHandler
    default void onItemHeld(PlayerItemHeldEvent event) {

    }

    @EventHandler
    default void onChat(AsyncChatEvent event) {

    }

    @EventHandler
    default void onCommand(PlayerCommandPreprocessEvent event) {

    }

    @EventHandler
    default void onAnimation(PlayerAnimationEvent event) {

    }

    @EventHandler
    default void onPlayerToggleSneak(PlayerToggleSneakEvent event){

    }

    @EventHandler
    default void onPlayerToggleSprint(PlayerToggleSprintEvent event){

    }

    @EventHandler
    default void onPlayerToggleFlight(PlayerToggleFlightEvent event){

    }

    @EventHandler
    default void onPlayerShear(PlayerShearEntityEvent event){

    }

    @EventHandler
    default void onPlayerExpChange(PlayerExpChangeEvent event){

    }

    @EventHandler
    default void onPlayerLevelChange(PlayerLevelChangeEvent event){

    }

    @EventHandler
    default void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event){

    }

    @EventHandler
    default void onPlayerTeleport(PlayerTeleportEvent event){

    }

    @EventHandler
    default void onPlayerPortal(PlayerPortalEvent event){

    }

    @EventHandler
    default void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event){

    }

    @EventHandler
    default void onPlayerEditBook(PlayerEditBookEvent event){

    }

    @EventHandler
    default void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event){

    }

    @EventHandler
    default void onPlayerUnleashEntity(PlayerUnleashEntityEvent event){

    }

    @EventHandler
    default void onPlayerRecipeDiscover(PlayerRecipeDiscoverEvent event){

    }

    @EventHandler
    default void onPlayerItemMend(PlayerItemMendEvent event){

    }

    @EventHandler
    default void onPlayerItemRiptide(PlayerRiptideEvent event){

    }

    @EventHandler
    default void onPlayerFish(PlayerFishEvent event){

    }

    @EventHandler
    default void onPlayerEggThrow(PlayerEggThrowEvent event){

    }

    @EventHandler
    default void onPlayerItemBreak(PlayerItemBreakEvent event){

    }

    @EventHandler
    default void onCommandSend(PlayerCommandSendEvent event){

    }

    @EventHandler
    default void onPlayerChangeWorld(PlayerChangedWorldEvent event){

    }

    @EventHandler
    default void onPlayerBedEnter(PlayerBedEnterEvent event){

    }

    @EventHandler
    default void onPlayerBedLeave(PlayerBedLeaveEvent event){

    }

    @EventHandler
    default void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){

    }

    @EventHandler
    default void onPlayerBucketFill(PlayerBucketFillEvent event){

    }

    @EventHandler
    default void onPlayerChangeMainHand(PlayerChangedMainHandEvent event){

    }

    @EventHandler
    default void onPlayerPickUpItem(PlayerAttemptPickupItemEvent event){

    }

    @EventHandler
    default void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event){

    }
}
