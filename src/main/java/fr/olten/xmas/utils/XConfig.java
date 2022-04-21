package fr.olten.xmas.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class XConfig {

    private File ymlFileConfig;
    private FileConfiguration ymlConfig;
    private File pluginDataFolder;
    private String name;

    public XConfig(File pluginDataFolder, String name) {

        StringBuilder fileName = new StringBuilder();
        fileName.append(name).append(".yml");
        this.name = fileName.toString();

        ymlFileConfig = new File(pluginDataFolder, this.name);
        this.pluginDataFolder = pluginDataFolder;
        ymlConfig = YamlConfiguration.loadConfiguration(ymlFileConfig);
    }

    public XConfig create() {
        if (!ymlFileConfig.exists()) {
            if (!this.pluginDataFolder.exists()) {
                this.pluginDataFolder.mkdir();
            }

            try {
                ymlFileConfig.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public File directory() {
        return pluginDataFolder;
    }

    public String name() {
        return name;
    }

    public File file() {
        return ymlFileConfig;
    }

    public FileConfiguration config() {
        return ymlConfig;
    }

    public XConfig addDefault(String key, String value) {
        if (ymlConfig.getString(key) == null) {

            ymlConfig.set(key, value);

            try {
                ymlConfig.save(ymlFileConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public XConfig addDefaultsMap(Map<String, Object> defaults) {
        for (String s : defaults.keySet()) {
            this.ymlConfig.set(s, defaults.get(s));
            try {
                ymlConfig.save(ymlFileConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public XConfig save() {
        try {
            ymlConfig.save(ymlFileConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public XConfig reload() {
        ymlConfig = YamlConfiguration.loadConfiguration(ymlFileConfig);
        return this;
    }

    public XConfig deleteFile() {
        if (ymlFileConfig.exists()) {
            ymlFileConfig.delete();
        }
        return this;
    }

    public void deleteParentFolder() {
        if (this.pluginDataFolder.exists()) {
            this.pluginDataFolder.delete();
        }
    }

    public void reset() {
        this.deleteFile();
        try {
            this.ymlFileConfig.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearDirectory() {
        this.directory().delete();
        this.pluginDataFolder.mkdir();
    }

    public void createChildDirectory(String name) throws IOException {
        if (!pluginDataFolder.exists()) {
            throw new IOException("The directory does not exist");
        }

        File childDirectory = new File(pluginDataFolder, name);

        if (childDirectory.exists()) {
            throw new IOException("The directory already exists");
        }

        childDirectory.mkdir();
    }

    public boolean containsValue(String value) {
        return ymlConfig.contains(value);
    }

}
