package fr.olten.xmas.ziplines;

import fr.olten.xmas.utils.XConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ZipLineManager {

    private Set<ZipLine> enabledZipLines,disabledZipLines;
    private static File zipLinesFolder;
    private XConfig config;

    public ZipLineManager(){
        enabledZipLines = Set.of();
        disabledZipLines = Set.of();
    }

    public void addRider(ZipLine zip, Player player){
        zip.setRider(player);
    }

    public void removeRider(ZipLine zip){
        zip.setRider(null);
    }

    public boolean isRider(ZipLine zip, Player player){
        return zip.getRider().equals(player);
    }

    public boolean isEnabled(ZipLine zip){
        return enabledZipLines.contains(zip);
    }

    public boolean isEnabled(String zipLineName){
        return enabledZipLines.stream().anyMatch(n -> n.getName() == zipLineName);
    }

    public boolean isDisabled(ZipLine zip){
        return disabledZipLines.contains(zip);
    }

    public boolean isDisabled(String zipLineName){
        return disabledZipLines.stream().anyMatch(n -> n.getName() == zipLineName);
    }

    /**
     * Setup all ziplines and add them to the list of registered Ziplines.
     * @param plugin
     */
    public void setup(Plugin plugin){
        plugin.getLogger().info("Setup is starting for plugin " + plugin.getName());
        zipLinesFolder = new File(plugin.getDataFolder(), "zipLines");

        if(!zipLinesFolder.exists()){
            plugin.getLogger().info("The folder for the ziplines doesn't exist. Creating it");
            zipLinesFolder.mkdirs();
            plugin.getLogger().info("Folder created.");
        }
        config = new XConfig(zipLinesFolder,"ZipLines");

        // Initializing ziplines.
        initZipLines();
    }

    // Init all ziplines and add them to the list.
    public void initZipLines(){
        config.create();
        config.reload();
        ConfigurationSection section = config.config().getConfigurationSection("zipLines");

        if(section != null){

            Set<ZipLine> zipLines = section.getKeys(false).stream().map(z -> new ZipLine(z,
                    new Location(
                            Bukkit.getWorld(section.getString("ziplines." + z + ".start.world_name")),
                            section.getDouble("ziplines." + z + ".start.x"),
                            section.getDouble("ziplines." + z + ".start.y"),
                            section.getDouble("ziplines." + z + ".start.z"),
                            (float) section.getDouble("ziplines." + z + ".start.yaw"),
                            (float) section.getDouble("ziplines." + z + ".start.pitch")
                    ),
                    new Location(
                            Bukkit.getWorld(section.getString("ziplines." + z + ".end.world_name")),
                            section.getDouble("ziplines." + z + ".end.x"),
                            section.getDouble("ziplines." + z + ".end.y"),
                            section.getDouble("ziplines." + z + ".end.z"),
                            (float) section.getDouble("ziplines." + z + ".end.yaw"),
                            (float) section.getDouble("ziplines." + z + ".end.pitch")
                    ),
                    section.getDouble("ziplines." + z + ".speed"),
                    section.getBoolean("ziplines." + z + ".enabled"),
                    null
            )).collect(Collectors.toSet());

            for(ZipLine zip : zipLines){
                if(zip.isEnabled()){
                    enabledZipLines.add(zip);
                    // Notifying that the zipline is enabled and registered.
                    Bukkit.getLogger().info("ZipLine " + zip.getName() + " registered and enabled.");
                }
                else{
                    disabledZipLines.add(zip);
                    Bukkit.getLogger().info("ZipLine " + zip.getName() + " added to the list of disabled ziplines.");
                }
            }

        }
        Bukkit.getLogger().info("Initialization finished, ready to use.");
    }

    /**
     * Push new zipline to the list of enabled ziplines.
     * @param zipLine
     */
    public void pushZipLine(ZipLine zipLine){
        enabledZipLines.add(zipLine);
        Bukkit.getLogger().info("New zipline named " + zipLine.getName() + " added to the list of enabled ziplines.");
    }

    /**
     *
     * Push new zipline to the list of enabled ziplines.
     * Creating a new zipline from variables.
     *
     * @param name -> The name of the zipline to be pushed.
     * @param start -> The location of the start of the zipline.
     * @param end -> The location of the end of the zipline.
     * @param speed -> The speed that the player will go using the zipline.
     */
    public void pushZipLine(String name, Location start, Location end, double speed){
        pushZipLine(new ZipLine(name,start,end,speed,true,null));
    }

    /**
     * Method to remove a zipline from the registered ziplines.
     * @param zipLineName -> The zipline name that must be disabled.
     */
    public void takeZipLine(String zipLineName){

        for(ZipLine zip : enabledZipLines){
            if(zip.getName().equalsIgnoreCase(zipLineName)){
                enabledZipLines.remove(zip);
                disabledZipLines.add(zip);
                Bukkit.getLogger().info("Switched zipline named " + zip.getName() + " from enabled to disabled list.");
            }
        }
    }

    /**
     * Method to remove a zipline from the list of ziplines.
     * @param zipLine -> The zipline that must be unregistered
     */
    public void takeZipLine(ZipLine zipLine){ takeZipLine(zipLine.getName()); }

    public Set<ZipLine> getEnabledZipLines(){ return enabledZipLines; }

    public Set<ZipLine> getDisabledZipLines(){ return disabledZipLines; }

    public void saveZipLines(){
        config.create();
        ConfigurationSection section = config.config().getConfigurationSection("ziplines");

        if(section == null){ section.createSection("ziplines"); }

        for(ZipLine zip : getEnabledZipLines()){

            if(!section.getKeys(false).contains(zip.getName())){
                // name of zip section.
                String zipLineConfLoc = "ziplines." + zip.getName();

                // Set start position.
                section.set(zipLineConfLoc + ".start.world_name","world");
                section.set(zipLineConfLoc + ".start.x",zip.getStart().getX());
                section.set(zipLineConfLoc + ".start.y",zip.getStart().getY());
                section.set(zipLineConfLoc + ".start.z",zip.getStart().getZ());
                section.set(zipLineConfLoc + ".start.yaw",zip.getStart().getYaw());
                section.set(zipLineConfLoc + ".start.pitch",zip.getStart().getPitch());

                // Set end position.
                section.set(zipLineConfLoc + ".end.world_name","world");
                section.set(zipLineConfLoc + ".end.x",zip.getEnd().getX());
                section.set(zipLineConfLoc + ".end.y",zip.getEnd().getY());
                section.set(zipLineConfLoc + ".end.z",zip.getEnd().getZ());
                section.set(zipLineConfLoc + ".end.yaw",zip.getEnd().getYaw());
                section.set(zipLineConfLoc + ".end.pitch",zip.getEnd().getPitch());

                // Set speed
                section.set(zipLineConfLoc + ".speed", zip.getSpeed());

                // Set enabled or disabled
                section.set(zipLineConfLoc + ".enabled",zip.isEnabled());
            }
        }
        config.save();
    }

}
