package fr.olten.xmas.elementary;

import com.google.common.base.Preconditions;
import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.ItemBuilder;
import fr.olten.xmas.utils.LocationSerialization;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class of recreated Elementary Land's shooting range.<br>
 * This class is initialized by the {@link fr.olten.xmas.Lobby} class.<br>
 * An instance is created each time the plugin starts and shouldn't be recreated or created twice.
 * @see Lobby#getShootingRange() the instance of this class.
 * @author Azodox_ (Luke)
 * 19/5/2022.
 */

public class ElementaryShootingRange {

    /**
     * Represents all players playing the game (our version supports many players).
     */
    private @Getter final List<Player> players = new ArrayList<>();
    /**
     * Represents all players that has played a game.<br>
     * Used to save and send statistics even if they leaved the game.
     */
    private final List<UUID> participants = new ArrayList<>();
    /**
     * Everywhere an armor stand can be summoned at.<br>
     * Defined in the config.yml file.
     */
    private @Getter final List<Location> spawningLocations;
    /**
     * Prefix used to send messages to players.<br>
     * Defined in the config.yml file.<br>
     * Using the {@link MiniMessage} class from Adventure library.
     */
    private final Component prefix;
    /**
     * This is the location where we teleport the player when he leaves or
     * the game is over.
     */
    private @Getter final Location outside;
    /**
     * This is the location where we teleport the player when he joins.
     */
    private final Location inside;
    /**
     * Limit of player playing at the same time.<br>
     * Defined in config.yml
     */
    private final int limit;
    /**
     * A {@link Lobby} instance provided by the constructor.
     */
    private @Getter(AccessLevel.PACKAGE) final Lobby lobby;
    /**
     * The {@link ItemBuilder} instance of the game's bow item.
     */
    private final ItemBuilder bow;
    /**
     * The {@link ItemBuilder} instance of the game's arrow item.
     */
    private final ItemBuilder arrow;
    private final @Getter @NotNull File folder;
    private BukkitTask task;
    private @Getter @Nullable ElementaryShootingRangeGame game;

    /**
     * Create an instance of {@link ElementaryShootingRange} class.<br>
     * This constructor is called by the {@link fr.olten.xmas.Lobby} class.<br>
     * <br>
     * <p>This constructor will put the lobby's instance provided into a class field.
     * And then, retrieve a lot from config.yml, such as outside and inside locations, the spawning locations and
     * the limit of players.</p>
     *
     * <br><p>Finally, it will set and create the bow and arrow item and generate the statistics folder (if not existing).</p>
     * @param lobby an instance of the plugin's main class.
     */
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

    /**
     * Make a player (given as parameter) join the current game or start a new one.<br>
     * <br>If the game is full, nothing will happen.<br>
     * If no game is running, a new game will be created and the player will be teleported to the inside location.<br>
     * If a game is running, the player will be teleported to the inside location and added to the game.<br>
     *
     * @see ElementaryShootingRangeGame Class to manage the game
     * @see ElementaryShootingRangeStatistics Class that manages the statistics
     * @param player the player to join the game.
     */
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

    /**
     * Make a player (given as parameter) leave the current game.<br>
     * If the player was the last one to leave, the game will automatically stop.
     * @param player the player to leave the game.
     */
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

    /**
     * Giving a bow and an arrow to a player which is in the game.
     * @param player the player to give the items.
     */
    public void giveItems(Player player){
        if(!players.contains(player))
            return;
        player.getInventory().addItem(bow.build());
        player.getInventory().addItem(arrow.build());
    }

    /**
     * Removing the bow and the arrow from a player which is in the game.<br>
     * This method will iterate through the player's inventory and remove the bow and the arrow
     * if an item equals to the bow or the arrow using {@link ItemStack#equals(Object)} method.
     * @param player the player to remove the items.
     */
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

    /**
     * Method called when the plugin is disabled.<br>
     * It will call {@link ElementaryShootingRange#endingGame()} if {@link ElementaryShootingRange#game} is not null
     * and cancel the task if it is not null.
     */
    public void pluginDisable(){
        if (game != null)
            this.endingGame();


        if(task != null)
            task.cancel();
    }

    /**
     * This method will be called to end the game and is usually called
     * when the game is over.<br>
     * It will (in the right order) :
     * <ul>
     *     <li>Check if game is not null (of course)</li>
     *     <li>Kill each armor stand</li>
     *     <li>Get every {@link ElementaryShootingRange#participants}</li>
     *     <li>For each participant, send them their statistics</li>
     *     <li>Save the statistics.</li>
     *     <li>Remove the participant from the list using {@link Iterator#remove()}</li>
     *     <li>Cancel game's task using {@link BukkitTask#cancel()}</li>
     *     <li>Set {@link ElementaryShootingRange#game} to null.</li>
     * </ul>
     */
    public void endingGame(){
        if(game == null)
            return;

        game.killAll();
        for(var pts = this.participants.iterator(); pts.hasNext();){
            var participant = pts.next();
            var player = Bukkit.getPlayer(participant);
            if(player != null && player.isOnline()){
                var uptime = game.getStatistics().getUptime(participant);
                var seconds = uptime.toSecondsPart();
                var minutes = uptime.toMinutesPart();
                var hours = uptime.toHoursPart();

                var formattedUptime = String.format("%02dh%02dm%02ds", hours, minutes, seconds);

                var killedStands = game.getStatistics().getKilledStands(participant);
                player.sendMessage(
                        prefix.append(Component.text(" Résumé de la dernière partie :")
                                .append(Component.newline())
                                .append(Component.text("Nombre de manches atteint : " + game.getStatistics().getBestScore()))
                                .append(Component.newline())
                                .append(Component.text(String.format("Tué%s : %d", killedStands <= 1 ? "" : "s", killedStands)))
                                .append(Component.newline())
                                .append(Component.text("Temps passé dans la partie : " + formattedUptime)))
                );
            }

            try {
                this.game.getStatistics().saveStatistic(participant);
                pts.remove();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.task.cancel();
        this.game = null;
    }

    /**
     * Called each time a player is added or removed from the game or
     * by {@link ElementaryShootingRangeGame#run()} if {@link ElementaryShootingRange#players} is empty.<br>
     * <br>
     * If {@link ElementaryShootingRange#players} is empty we cancel the task using {@link BukkitTask#cancel()}
     */
    public void checkTask(){
        if(players.isEmpty())
            task.cancel();
    }

    /**
     * Run {@link ElementaryShootingRange#task} which represents the {@link BukkitTask} that handles {@link ElementaryShootingRangeGame#run()}<br>
     * <br>If {@link ElementaryShootingRange#game} is null it will throw a {@link NullPointerException}
     * <br>Code is also stopped if {@link ElementaryShootingRange#players} is empty.
     * <br>If everything is ok, the task will be scheduled using {@link org.bukkit.scheduler.BukkitScheduler#runTaskTimer(Plugin, Runnable, long, long)}
     * @see ElementaryShootingRange#checkTask() Method to check if the task must be canceled.
     */
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
