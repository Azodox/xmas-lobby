package fr.olten.xmas.ziplines;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ZipLine {

    private String name;
    private Location start;
    private Location end;
    private double speed;
    private boolean enabled;
    private Player rider;

    public ZipLine(String name, Location start, Location end, double speed, boolean enabled, Player rider){
        this.name = name;
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.enabled = enabled;
        this.rider = rider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRider(Player rider) {
        this.rider = rider;
    }

    public String getName() {
        return name;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Player getRider() {
        return rider;
    }


}
