package fr.olten.xmas.carousel;

import fr.mrmicky.fastparticles.ParticleType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Random;

public final class CarouselPlayerManager {

    private static final ParticleType MAGIC = ParticleType.of("SPELL_WITCH");
    private final Carousel carousel;

    public CarouselPlayerManager(Carousel carousel) {
        this.carousel = carousel;
    }

    public void ride(Player player){
        if(carousel.getHorses().stream().noneMatch(horse -> horse.getPassengers().isEmpty())){
            player.sendMessage(Carousel.PREFIX.append(Component.text(" Il n'y a plus de chevaux disponibles.").color(NamedTextColor.RED)));
            return;
        }
        var random = new Random();
        var ride = carousel.getHorses().stream().filter(horse -> horse.getPassengers().isEmpty()).map(horse -> (ArmorStand) carousel.getHorseSeats().get(horse.getUniqueId())).toList();
        this.ride(player, ride.get(random.nextInt(ride.size())));
    }

    public void ride(Player player, ArmorStand ride){
        if(ride == null){
            return;
        }
        carousel.getRiders().add(player);

        MAGIC.spawn(player.getLocation().getWorld(), player.getLocation(), 50, 0.1, 0.3, 0.1, 0);
        ride.addPassenger(player);
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, (float) 0.5, (float) 0.1);
        MAGIC.spawn(ride.getLocation().getWorld(), ride.getLocation().add(0, 1, 0), 50, 0.1, 0.3, 0.1, 0);
    }

    public void dismount(Player player){
        carousel.getRiders().remove(player);
        player.teleport(carousel.getCallbackLocation());
    }

    public boolean isRiding(Player player){
        return carousel.getRiders().contains(player);
    }
}
