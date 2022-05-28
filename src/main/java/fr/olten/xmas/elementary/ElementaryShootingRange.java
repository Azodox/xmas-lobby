package fr.olten.xmas.elementary;

import com.google.common.base.Preconditions;
import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.ItemBuilder;
import fr.olten.xmas.utils.LocationSerialization;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Azodox_ (Luke)
 * 19/5/2022.
 */

public class ElementaryShootingRange {

    private @Getter final List<Player> players = new ArrayList<>();
    private final List<UUID> participants = new ArrayList<>();
    private @Getter final List<Location> spawningLocations;
    private final Component prefix;
    private final Location outside;
    private final Location inside;
    private final int limit;
    private @Getter(AccessLevel.PACKAGE) final Lobby lobby;
    private final ItemBuilder bow;
    private final ItemBuilder arrow;
    private final @Getter @NotNull File folder;
    private BukkitTask task;
    private @Getter @Nullable ElementaryShootingRangeGame game;

    public ElementaryShootingRange(Lobby lobby) {
        this.lobby = lobby;
        this.prefix = MiniMessage.miniMessage().deserialize(Objects.requireNonNull(lobby.getConfig().getString("shootingRange.prefix"), "Prefix cannot be null!"));
        this.outside = LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("shootingRange.outsideLocation")));
        this.inside = LocationSerialization.deserialize(Objects.requireNonNull(lobby.getConfig().getString("shootingRange.insideLocation")));
        this.spawningLocations = lobby.getConfig().getStringList("shootingRange.spawningLocations").stream().map(LocationSerialization::deserialize).toList();
        this.limit = lobby.getConfig().getInt("shootingRange.limit");
        this.bow = new ItemBuilder(Material.BOW, 1).displayname("§bBow").enchant(Enchantment.ARROW_INFINITE, 1).unbreakable(true).flag(ItemFlag.HIDE_ENCHANTS).flag(ItemFlag.HIDE_UNBREAKABLE);
        this.arrow = new ItemBuilder(Material.ARROW, 1);
        this.folder = new File(lobby.getDataFolder(), "shootingRange");
        if(!folder.exists())
            folder.mkdir();
    }

    public void join(Player player){
        if(players.contains(player) || players.size() >= limit)
            return;
        players.add(player);
        participants.add(player.getUniqueId());
        player.teleport(inside);
        this.giveItems(player);
        if(players.size() == 1) {
            this.game = new ElementaryShootingRangeGame(this);
            this.runTask();
        } else
            this.checkTask();
    }

    public void leave(Player player){
        if(!players.contains(player))
            return;
        this.removeItems(player);
        players.remove(player);
        player.teleport(outside);
        this.checkTask();
        if (players.isEmpty())
            this.endingGame();
    }

    public void giveItems(Player player){
        if(!players.contains(player))
            return;
        player.getInventory().addItem(bow.build());
        player.getInventory().addItem(arrow.build());
    }

    public void removeItems(Player player){
        if(!players.contains(player))
            return;
        for (ItemStack content : player.getInventory().getContents()) {
            if(content == null)
                return;

            if(content.getType() == Material.BOW || content.getType() == Material.ARROW){
                if(content.equals(bow.build()) || content.equals(arrow.build()))
                    player.getInventory().remove(content);
            }
        }
    }

    public void pluginDisable(){
        if (game != null)
            game.getArmorStands().forEach(game::kill);


        if(task != null)
            task.cancel();
    }

    public void endingGame(){
        if(game == null)
            return;

        for(Iterator<ArmorStand> armorStand = game.getArmorStands().iterator(); armorStand.hasNext();) {
            ArmorStand stand = armorStand.next();
            game.kill(stand);
            armorStand.remove();
        }
        this.participants.forEach(uuid -> {
            var player = Bukkit.getPlayer(uuid);
            if(player != null && player.isOnline()){
                player.sendMessage(
                        prefix.append(Component.text(" Résumé de la dernière partie :")
                                .append(Component.newline())
                                .append(Component.text("Nombre de manches atteint : " + game.getStatistics().getBestScore()).style(Style.empty()))
                                .append(Component.newline())
                                .append(Component.text(String.format("Tué%s : %d", game.getStatistics().getBestScore() <= 1 ? "" : "s", game.getStatistics().getKilledStands(uuid))))
                                .append(Component.newline())
                                .append(Component.text("Temps passé dans la partie : " + game.getStatistics().getUptime(uuid).getSeconds() + " secondes")))
                );
            }

            try {
                this.game.getStatistics().saveStatistic(uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.game = null;
    }

    public void checkTask(){
        if(players.isEmpty())
            task.cancel();
    }

    public void runTask(){
        Preconditions.checkNotNull(game, "Game has not started yet");
        if(players.isEmpty())
            return;
        task = lobby.getServer().getScheduler().runTaskTimer(lobby, game, 0, 20);
    }

    public Set<Location> getJoiningSigns(){
        return lobby.getConfig().getStringList("shootingRange.joinSigns").stream().map(LocationSerialization::deserialize).collect(Collectors.toSet());
    }

    public Set<Location> getLeavingSigns(){
        return lobby.getConfig().getStringList("shootingRange.leaveSigns").stream().map(LocationSerialization::deserialize).collect(Collectors.toSet());
    }
}
