package fr.olten.xmas.elementary;

import fr.mrmicky.fastparticles.ParticleType;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.*;

/**
 * @author Azodox_ (Luke)
 * 21/5/2022.
 */

public class ElementaryShootingRangeGame implements Runnable {

    private final ElementaryShootingRange shootingRange;
    private final ElementaryShootingRangeStatistics statistics;
    private final List<ArmorStand> armorStands = new ArrayList<>();
    private final String id;
    private int round;
    @Getter private boolean running;

    public ElementaryShootingRangeGame(ElementaryShootingRange shootingRange) {
        this.shootingRange = shootingRange;
        this.statistics = new ElementaryShootingRangeStatistics(shootingRange);
        this.id = UUID.randomUUID().toString().substring(7, 12).replace("-", "").toUpperCase();
    }

    @Override
    public void run() {
        if(shootingRange.getPlayers().isEmpty()){
            shootingRange.checkTask();
            return;
        }

        shootingRange.getPlayers().forEach(player -> statistics.addUptime(player, Duration.ofSeconds(1)));

        if(round == 0)
            incrementRound();

        if(round >= 1){
            if(!running)
                start();
            this.moveStands();
            if (this.statistics.getBestScore() < round)
                this.statistics.setBestScore(round);
        }
    }

    public void incrementRound(){
        if(running)
            return;

        round++;
        shootingRange.getPlayers().forEach(player -> player.showTitle(Title.title(Component.text("Manche")
                        .color(TextColor.lerp(0.8f, TextColor.fromHexString("#4c42ff"), TextColor.fromHexString("#e31e59"))),
                Component.text("#" + round).color(TextColor.fromHexString("#e31e59")).decorate(TextDecoration.BOLD),
                Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(500), Duration.ofMillis(200)))));
    }

    public void nextRound(){
        running = false;
        incrementRound();
    }

    public void start(){
        if (running)
            return;

        this.spawn();
        this.running = true;
    }

    public void standTouchedLine() {
        if (!running)
            return;

        for(var players = shootingRange.getPlayers().iterator(); players.hasNext();) {
            var player = players.next();
            if(player.isOnline()) {
                player.teleport(shootingRange.getOutside());
                shootingRange.removeItems(player);
                players.remove();
            }
        }
        shootingRange.endingGame();
    }

    private void moveStands(){
        if(armorStands.isEmpty())
            return;

        var random = new Random();

        armorStands.forEach(armorStand -> {
            var south = armorStand.getLocation().getBlock().getRelative(BlockFace.SOUTH);
            var north = armorStand.getLocation().getBlock().getRelative(BlockFace.NORTH);
            var west = armorStand.getLocation().getBlock().getRelative(BlockFace.WEST);
            var eye = armorStand.getEyeLocation().getBlock().getRelative(BlockFace.WEST);

            double z = Double.parseDouble((random.nextBoolean() ? "-" : "") + 0.30d);
            if(west.getType() != Material.AIR && south.getType() == Material.AIR && north.getType() == Material.AIR && eye.getType() == Material.AIR) {
                armorStand.setVelocity(new Vector(-0.45, 1, 0));
            } else if(west.getType() != Material.AIR && (south.getType() == Material.AIR && north.getType() != Material.AIR || north.getType() == Material.AIR && south.getType() != Material.AIR)) {
                if(armorStand.getLocation().add(0, 0, -2).getBlock().getRelative(BlockFace.NORTH).getType() == Material.AIR)
                    armorStand.setVelocity(new Vector(0, 0, -0.5));
                else if(armorStand.getLocation().add(0, 0, 2).getBlock().getRelative(BlockFace.SOUTH).getType() == Material.AIR)
                    armorStand.setVelocity(new Vector(0, 0, 0.5));
                else
                    armorStand.setVelocity(new Vector(-0.25, 0.50, 0));
            } else if(west.getType() == Material.AIR && south.getType() == Material.AIR && north.getType() == Material.AIR && eye.getType() == Material.AIR) {
                armorStand.setVelocity(new Vector(-0.15, 0, 0));
            } else {
                armorStand.setVelocity(new Vector(-0.30, 0, z));
            }
        });
    }

    private void spawn(){
        if (running)
            return;

        var random = new Random();
        var amount = round == 1 ? round : random.nextInt(1, 4);

        var sound = Sound.BLOCK_NOTE_BLOCK_BIT;

        for (int i = 0; i < amount - 1; i++) {
            var location = shootingRange.getSpawningLocations().get(random.nextInt(shootingRange.getSpawningLocations().size()));
            var armorStand = spawnArmorStand(location);
            armorStand.setSmall(random.nextBoolean());
            if (armorStand.isSmall()) {
                Bukkit.getScheduler().runTaskLater(shootingRange.getLobby(), () -> kill(armorStand), 100L);
            }
        }
        spawnArmorStand(shootingRange.getSpawningLocations().get(random.nextInt(shootingRange.getSpawningLocations().size())));
        shootingRange.getPlayers().forEach(player -> player.playSound(player, sound, 1.0f, 1.0f));
    }

    public void killAll(){
        if(!running)
            return;

        running = false;
        for(Iterator<ArmorStand> armorStand = this.getArmorStands().iterator(); armorStand.hasNext();) {
            ArmorStand stand = armorStand.next();
            this.kill(stand);
            armorStand.remove();
        }
    }

    public void kill(ArmorStand armorStand){
        if(armorStand == null)
            return;

        armorStand.setHealth(0.0d);
        armorStand.remove();
    }

    public void hasBeenKilled(ArmorStand armorStand, Player killer){
        armorStands.remove(armorStand);

        this.getStatistics().addKilledStand(killer);
        killer.playSound(killer, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);

        if(armorStands.isEmpty())
            this.nextRound();
    }

    private ArmorStand spawnArmorStand(Location location){
        var particle = ParticleType.of("CLOUD");
        var random = new Random();
        var armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(true);
        armorStand.setGravity(true);
        armorStand.setBasePlate(false);
        armorStand.setArms(random.nextBoolean());
        armorStand.setRotation(location.getYaw(), location.getPitch());
        armorStand.customName(random.nextBoolean() ? Component.text("Dinnerbone") : Component.empty());
        armorStand.setCustomNameVisible(false);
        armorStand.setSwimming(random.nextBoolean());
        armorStand.setGliding(random.nextBoolean());
        armorStand.setGlowing(random.nextBoolean());
        armorStand.setInvulnerable(false);
        armorStand.setHealth(20.0);
        armorStand.setArms(true);
        armorStands.add(armorStand);

        particle.spawn(location.getWorld(), armorStand.getEyeLocation(), 1, 0.2, 0.5, 0.2, 0);
        return armorStand;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public ElementaryShootingRangeStatistics getStatistics() {
        return statistics;
    }

    public String getId() {
        return id;
    }
}
