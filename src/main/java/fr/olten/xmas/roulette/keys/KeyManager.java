package fr.olten.xmas.roulette.keys;

import fr.olten.xmas.Lobby;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KeyManager {

    private ItemStack commonKey;
    private ItemStack uncommonKey;
    private ItemStack rareKey;
    private ItemStack epicKey;
    private ItemStack legendaryKey;
    private ItemStack mythicalKey;

    private Lobby main;

    public KeyManager(Lobby main) {
        this.main = main;

        commonKey = new ItemStack(Material.LEVER);
        uncommonKey = new ItemStack(Material.END_ROD);
        rareKey = new ItemStack(Material.TRIPWIRE_HOOK);
        epicKey = new ItemStack(Material.NAME_TAG);
        legendaryKey = new ItemStack(Material.BLAZE_ROD);
        mythicalKey = new ItemStack(Material.NETHER_STAR);
    }

    public Key getKey(KeyTypes type) {
        Key key;
        switch(type) {
            case OLTEN_KEY -> key = new Key(legendaryKey,KeyTypes.OLTEN_KEY, "OLTEN_KEY");
            case XMAS_KEY -> key =new Key(commonKey,KeyTypes.XMAS_KEY, "XMAS_KEY");
            case VALNEAS_KEY -> key = new Key(epicKey,KeyTypes.VALNEAS_KEY, "VALNEAS_KEY");
            case ELEMENTARY_KEY -> key = new Key(epicKey,KeyTypes.ELEMENTARY_KEY, "ELEMENTARY_KEY");
            case ELGARDIAH_KEY -> key = new Key(rareKey,KeyTypes.ELGARDIAH_KEY, "ELGARDIAH_KEY");
            case LUKE_KEY -> key = new Key(uncommonKey,KeyTypes.LUKE_KEY, "LUKE_KEY");
            case RAYNALD_KEY -> key = new Key(mythicalKey,KeyTypes.RAYNALD_KEY, "RAYNALD_KEY");
            case MICHEL_KEY -> key = new Key(legendaryKey,KeyTypes.MICHEL_KEY, "MICHEL_KEY");
            case MATT_KEY -> key = new Key(uncommonKey,KeyTypes.MATT_KEY, "MATT_KEY");
            default -> key = new Key(commonKey,KeyTypes.XMAS_KEY, "XMAS_KEY");
        }
        return key;
    }

    // Give a key to a player
    public void giveKey(Player player, KeyTypes type) {
        KeyDAL dal = new KeyDAL(main);
        dal.addKey(player, type);
        player.sendMessage(Component.text("Vous avez reçu une clé !"));
        Bukkit.getLogger().info("[Keys] " + player.getName() + " has received a " + type.name() + " key.");
    }

    // Remove a key from a player
    public void removeKey(Player player, KeyTypes type) {
        KeyDAL dal = new KeyDAL(main);
        dal.removeKey(player, type);
        player.sendMessage(Component.text("Vous avez perdu une clé !"));
        Bukkit.getLogger().info("[Keys] " + player.getName() + " lost a " + type.name() + " key.");
    }

    public void setKey(Player player, KeyTypes type,int amount){
        KeyDAL dal = new KeyDAL(main);
        dal.setKey(player, type,amount);
        player.sendMessage(Component.text("Votre nombre de clés a changé !"));
        Bukkit.getLogger().info("[Keys] " + player.getName() + " keys of type " + type.name() + " was changed to " + amount + ".");
    }

    // Clear the keys of a player
    public void clearKeys(Player player) {
        KeyDAL dal = new KeyDAL(main);
        dal.resetKeys(player);
        player.sendMessage(Component.text("Vous avez perdu toutes vos clés !"));
        Bukkit.getLogger().info("[Keys] " + player.getName() + " was cleared of all keys.");
    }

}
