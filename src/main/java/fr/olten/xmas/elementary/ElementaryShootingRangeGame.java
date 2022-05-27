package fr.olten.xmas.elementary;

import fr.mrmicky.fastparticles.ParticleType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    private boolean running;

    public ElementaryShootingRangeGame(ElementaryShootingRange shootingRange) {
        this.shootingRange = shootingRange;
        this.statistics = new ElementaryShootingRangeStatistics(shootingRange);
        this.id = UUID.randomUUID().toString().substring(7, 12).toUpperCase();
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

    private void moveStands(){
        if(armorStands.isEmpty())
            return;

        var random = new Random();

        armorStands.forEach(armorStand -> {
            armorStand.setVelocity(new Vector(random.nextDouble(), 0, random.nextDouble()));
        });
    }

    private void spawn(){
        if (running)
            return;

        var random = new Random();
        var amount = round == 1 ? round : random.nextInt(1, 4);

        var particle = ParticleType.of("CLOUD");
        var sound = Sound.BLOCK_NOTE_BLOCK_BIT;

        for (int i = 0; i < amount; i++) {
            var location = shootingRange.getSpawningLocations().get(random.nextInt(shootingRange.getSpawningLocations().size()));
            var armorStand = spawnArmorStand(location);
            if (armorStand.isSmall()) {
                Bukkit.getScheduler().runTaskLater(shootingRange.getLobby(), () -> kill(armorStand), 100L);
            }
            particle.spawn(location.getWorld(), armorStand.getEyeLocation(), 1, 0.2, 0.5, 0.2, 0);
        }
        shootingRange.getPlayers().forEach(player -> player.playSound(player, sound, 1.0f, 1.0f));
    }

    public void kill(ArmorStand armorStand){
        if(armorStand == null)
            return;

        armorStand.setHealth(0.0d);
        armorStand.remove();
    }

    public void hasBeenKilled(ArmorStand armorStand){
        armorStands.remove(armorStand);
        if(armorStand.getKiller() != null)
            armorStand.getKiller().playSound(armorStand.getKiller(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);

        if(armorStands.isEmpty())
            this.nextRound();
    }

    private ArmorStand spawnArmorStand(Location location){
        var random = new Random();
        var armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(true);
        armorStand.setGravity(true);
        armorStand.setBasePlate(false);
        armorStand.setArms(random.nextBoolean());
        armorStand.setRotation(random.nextFloat(), random.nextFloat());
        armorStand.customName(random.nextBoolean() ? Component.text("Dinnerbone") : Component.empty());
        armorStand.setCustomNameVisible(false);
        armorStand.setSwimming(random.nextBoolean());
        armorStand.setGliding(random.nextBoolean());
        armorStand.setGlowing(random.nextBoolean());
        armorStand.setInvulnerable(false);
        armorStand.setSmall(random.nextBoolean());
        armorStand.setHealth(20.0);
        armorStands.add(armorStand);
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
