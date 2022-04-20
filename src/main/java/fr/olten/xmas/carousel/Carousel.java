package fr.olten.xmas.carousel;

import com.google.common.collect.ImmutableList;
import fr.mrmicky.fastparticles.ParticleType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftArmorStand;
import org.bukkit.entity.*;

import java.util.*;

public class Carousel {

    static final Component PREFIX = MiniMessage.miniMessage().deserialize("<bold><white>[</white><rainbow:!2>CAROUSEL</rainbow><white>]</white></bold>");

    private static final int MAX_HEIGHT = 1;
    private static final ParticleType FIREWORK_SPARK = ParticleType.of("fireworks_spark");
    /**
     * The list of players in the carousel.
     * @see this#getRiders() for the list of players.
     */
    private final List<Player> riders = new ArrayList<>();

    private final Map<UUID, Entity> horseSeats = new HashMap<>();
    private final List<ArmorStand> armorStands = new ArrayList<>();

    private final Location center;
    private final Location callbackLocation;
    private final Sign joiningSign;

    /**
     * The list of horses in the carousel.
     * @see this#getHorses() for the list of horses.
     */
    private final List<Horse> horses = new ArrayList<>();

    private final int carCount;
    private final int radius;
    private final boolean clockwise;
    private float speed = 0.0F;
    private float degrees = 0.0F;

    public Carousel(Location center, Location callbackLocation, Sign joiningSign) {
        this.center = center;
        this.callbackLocation = callbackLocation;
        this.joiningSign = joiningSign;

        this.joiningSign.line(1, PREFIX);
        this.joiningSign.line(2, Component.text("Rejoindre").color(NamedTextColor.WHITE));
        this.joiningSign.update();

        this.carCount = 6;
        this.clockwise = true;
        this.radius = 4;
        this.speed = 2F;
        this.spawn();
    }

    public void spawn() {
        for (int i = 0; i < this.carCount; i++) {
            float degrees = (360 / this.carCount * i);

            var carLoc = this.degrees(this.center, this.radius, degrees);
            carLoc.setYaw(degrees);
            if (!this.clockwise)
                carLoc.setYaw(degrees + 180.0F);

            double height = Math.cos(Math.toRadians((degrees * 4.0F))) * MAX_HEIGHT;
            if (this.carCount % 2 != 0 && i % 2 == 0)
                height = 0.0D - height;
            carLoc.add(0.0D, height, 0.0D);

            var standLoc = carLoc.clone();
            standLoc.subtract(0.0D, 0.25D, 0.0D);

            var car = this.spawnArmorStand(standLoc, Component.text("Carousel_").append(Component.text(i)));
            var horse = (Horse) carLoc.getWorld().spawnEntity(carLoc, EntityType.HORSE);

            horse.setGravity(false);
            horse.setAdult();
            horse.setInvulnerable(true);
            horse.setSilent(true);
            horse.setAI(false);
            horse.setCollidable(false);
            horse.setInvisible(false);
            this.armorStands.add(car);
            this.horseSeats.put(horse.getUniqueId(), car);
            this.horses.add(horse);
        }
    }
    public void despawn() {
        this.speed = 0.0F;
        for (ArmorStand stand : this.armorStands) {
            stand.remove();
        }
        for (Horse horse : this.horses) {
            horse.remove();
            horseSeats.remove(horse.getUniqueId());
        }
        this.horses.clear();
        this.armorStands.clear();
    }

    public void update() {
        if (this.speed == 0.0F)
            return;
        for (int i = 0; i < this.carCount; i++) {
            float startAngle = (360 / this.carCount * i);
            float finalAngle = this.degrees + startAngle;
            double height = Math.cos(Math.toRadians((finalAngle * 4.0F))) * MAX_HEIGHT;
            if (this.carCount % 2 != 0 && i % 2 == 0)
                height = 0.0D - height;

            var center = this.center.clone().add(0.0D, height, 0.0D);
            var loc = this.degrees(center, this.radius, finalAngle);

            if (!this.clockwise)
                finalAngle += 180.0F;
            loc.setYaw(finalAngle);

            var car = this.armorStands.get(i);
            var standLoc = loc.clone();

            standLoc.subtract(0.0D, 0.25D, 0.0D);
            ((CraftArmorStand) car).getHandle().b(standLoc.getX(), standLoc.getY(), standLoc.getZ(), loc.getYaw(), loc.getPitch());

            var horse = this.horses.get(i);
            horse.teleport(loc);
            FIREWORK_SPARK.spawn(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), 1, 0, 0, 0, 0, 0);
            FIREWORK_SPARK.spawn(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ(), 1, 0, 0, 0, 0, 0);
        }
        if (this.clockwise) {
            this.degrees += this.speed;
        } else {
            this.degrees -= this.speed;
        }
    }

    private Location degrees(Location loc, double radius, double dgrs) {
        double x = loc.getX() + radius * Math.cos(dgrs * Math.PI / 180.0D);
        double y = loc.getY();
        double z = loc.getZ() + radius * Math.sin(dgrs * Math.PI / 180.0D);
        return new Location(loc.getWorld(), x, y, z);
    }

    private ArmorStand spawnArmorStand(Location loc, Component name) {
        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.customName(name);
        return stand;
    }

    public CarouselPlayerManager manager(){
        return new CarouselPlayerManager(this);
    }

    public boolean canBeRidden(Horse horse){
        if(horses.contains(horse)){
            return horse.getPassengers().isEmpty();
        }
        return false;
    }

    public boolean isRide(Entity entity){
        if(entity instanceof ArmorStand armorStand){
            return getHorseSeats().containsValue(armorStand);
        }
        return false;
    }

    public Map<UUID, Entity> getHorseSeats() {
        return horseSeats;
    }

    public ImmutableList<Horse> getHorses() {
        return ImmutableList.copyOf(horses);
    }

    public Location getCenter() {
        return center;
    }

    List<Player> getRiders() {
        return riders;
    }

    public Sign getJoiningSign() {
        return joiningSign;
    }

    public Location getCallbackLocation() {
        return callbackLocation;
    }
}
