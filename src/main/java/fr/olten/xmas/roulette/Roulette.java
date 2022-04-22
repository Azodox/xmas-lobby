package fr.olten.xmas.roulette;

import fr.olten.xmas.Lobby;
import fr.olten.xmas.carousel.CarouselPlayerManager;
import fr.olten.xmas.rewards.KeyReward;
import fr.olten.xmas.rewards.ObjetReward;
import fr.olten.xmas.rewards.Reward;
import fr.olten.xmas.rewards.RewardHandler;
import fr.olten.xmas.roulette.keys.KeyTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Roulette {

    private final Map<Integer,Location> blockOrderAndLocation = new HashMap<>(16);
    private int currentBlockPosition, amountOfTurns,slowCount = 0,randomEndNumber;
    private boolean running;
    private Player currentlyTurningRoulette;
    private KeyTypes currentKeyType;
    private int taskIDStart,taskIDSlow;

    private final Lobby lobby;

    private Random r;

    private final Sign startSign;

    // Create a roulette with no location predefined.
    public Roulette(Sign startSign, Lobby lobby) {
        this.currentKeyType = KeyTypes.XMAS_KEY;
        this.lobby = lobby;
        this.startSign = startSign;
        currentBlockPosition = 0;
        running = false;

        r = new Random();
    }

    // Create a roulette with a set of location.
    public Roulette(Sign startSign, List<Location> blockLocations, Lobby lobby) {
        this.currentKeyType = KeyTypes.XMAS_KEY;
        this.lobby = lobby;
        this.startSign = startSign;
        currentBlockPosition = 0;
        int i = 0;
        for(Location location : blockLocations){
            if(i < 16){
                blockOrderAndLocation.put(i,location);
                i++;
            }
            else{
                Bukkit.getLogger().severe("[Roulette] Too many blocks in the roulette. Only 16 blocks are allowed.");
            }
        }
        running = false;

        r = new Random();
    }

    public RoulettePlayerManager manager(){
        return new RoulettePlayerManager(this);
    }

    public boolean isRunning(){
        return running;
    }

    public Map<Integer,Location> getBlocksOrder() { return blockOrderAndLocation; }

    public Player currentPlayer() { return currentlyTurningRoulette; }
    public void currentPlayer(Player player) { currentlyTurningRoulette = player; }

    public Sign getStartSign(){
        return startSign;
    }

    public void nextBlock(){
        if(amountOfTurns > 3){
            startSlow();
            return;
        }

        Block block = blockOrderAndLocation.get(currentBlockPosition).getBlock();
        block.setType(Material.WHITE_WOOL);


        // If the current position exceeds the maximum, reset it to 0.
        if(currentBlockPosition > 14){
            currentBlockPosition = 0;
            amountOfTurns++;
        }
        else{
            currentBlockPosition++;
        }

        block = blockOrderAndLocation.get(currentBlockPosition).getBlock();

        // Set new block in position to green wool.
        block.setType(Material.GREEN_WOOL);
        currentlyTurningRoulette.playSound(currentlyTurningRoulette, Sound.BLOCK_NOTE_BLOCK_PLING, (float) 0.5, (float) 0.1);

    }

    public void nextBlockSlow(){
        if(slowCount >= randomEndNumber){
            // Reward the player.
            reward();
            // Stop the roulette.
            stop();
            return;
        }

        Block block = blockOrderAndLocation.get(currentBlockPosition).getBlock();
        block.setType(Material.WHITE_WOOL);

        currentBlockPosition++;
        slowCount++;

        block = blockOrderAndLocation.get(currentBlockPosition).getBlock();

        // Set new block in position to green wool.
        block.setType(Material.GREEN_WOOL);
        currentlyTurningRoulette.playSound(currentlyTurningRoulette, Sound.BLOCK_NOTE_BLOCK_PLING, (float) 0.5, (float) 0.1);
    }

    // Start the roulette
    public void start(KeyTypes keyType){
        this.currentKeyType = keyType;
        currentBlockPosition = 0;
        running = true;

        // Start the scheduler (Start the roulette first turn).
        taskIDStart = Bukkit.getScheduler().scheduleSyncRepeatingTask(lobby, new Runnable() {
            @Override
            public void run() { nextBlock(); }
        }, 0L, (long) (20*0.05));
    }

    public void startSlow(){
        Bukkit.getScheduler().cancelTask(taskIDStart);

        int low = 1;
        int high = 9;
        randomEndNumber = r.nextInt(high-low) + low;

        // Start the scheduler (Start the roulette but slow).
        taskIDSlow = Bukkit.getScheduler().scheduleSyncRepeatingTask(lobby, new Runnable() {
            @Override
            public void run() { nextBlockSlow(); }
        }, 0L, (long) (20*0.3));

    }

    public void stop(){
        // Cancel the task.
        Bukkit.getScheduler().cancelTask(taskIDSlow);

        // Reset all blocks to white wool.
        getBlocksOrder().forEach((position, location) -> {
            Block block = blockOrderAndLocation.get(position).getBlock();
            block.setType(Material.WHITE_WOOL);
        });

        // Set the amount of turns to 0.
        amountOfTurns = 0;

        getBlocksOrder().forEach((position, location) -> {
            Block block = blockOrderAndLocation.get(position).getBlock();
            block.setType(Material.YELLOW_WOOL);
        });

        spawnFireworks(startSign.getLocation().add(0, 5, 0),2);

        Bukkit.getLogger().info("Stopped roulette at position: " + currentBlockPosition);

        currentlyTurningRoulette.playSound(currentlyTurningRoulette, Sound.BLOCK_NOTE_BLOCK_PLING, (float) 0.5, (float) 9);

        running = false;

        currentPlayer(null);
    }

    public void reward(){
        KeyReward reward = new KeyReward(Reward.RewardType.KEYREWARD,currentKeyType);
        RewardHandler.receiveReward(currentlyTurningRoulette,reward);
    }

    public static void spawnFireworks(Location location, int amount){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}
