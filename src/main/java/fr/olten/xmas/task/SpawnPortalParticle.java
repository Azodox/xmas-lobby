package fr.olten.xmas.task;

import fr.mrmicky.fastparticles.ParticleType;
import fr.olten.xmas.Lobby;
import fr.olten.xmas.utils.LocationSerialization;
import org.bukkit.util.Vector;

/**
 * @author Azodox_ (Luke)
 * 14/5/2022.
 */

public class SpawnPortalParticle implements Runnable {

    private final ParticleType particleType = ParticleType.of("WHITE_ASH");
    private final Lobby lobby;
    public SpawnPortalParticle(Lobby lobby) {
        this.lobby = lobby;
    }

    private int i = 0;

    @Override
    public void run() {
        var locationString = lobby.getConfig().getString("portalToVillageCenter");
        if(locationString == null) {
            return;
        }

        var location = LocationSerialization.deserialize(locationString);

        if (i >= 0 && i <= 360) {
            i++;
        }else if(i >= 360){
            i = 0;
        }

        for (int degree = 0; degree < 180; degree++) {
            double radians = Math.toRadians(degree);
            double x = 4 * Math.cos(radians);
            double z = 4 * Math.sin(radians);
            var vector = rotateAroundAxisX(new Vector(x, 0, z), 90);
            vector = rotateAroundAxisZ(vector, 180);
            location.add(vector.getX(), vector.getY(), vector.getZ());
            particleType.spawn(location.getWorld(), location.getX(), location.getY(), location.getZ(), 1,
                    0.25, -1, 0.5);
            location.subtract(vector.getX(), vector.getY(), vector.getZ());
        }

    }

    private Vector rotateAroundAxisX(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private Vector rotateAroundAxisZ(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
