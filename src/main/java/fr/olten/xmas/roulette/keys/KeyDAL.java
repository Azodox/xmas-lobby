package fr.olten.xmas.roulette.keys;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.olten.xmas.Lobby;
import org.bson.Document;
import org.bukkit.entity.Player;

public class KeyDAL {

    private final Lobby main;
    private final MongoClient mongo;

    public KeyDAL(Lobby main) {
        this.main = main;
        this.mongo = main.getMongo().getMongoClient();
    }

    private MongoDatabase getDatabase(){
        return mongo.getDatabase(main.getConfig().getString("mongodb.database"));
    }

    private MongoCollection<Document> getKeyCollection() {
        return getDatabase().getCollection("player_keys");
    }

    public void initPlayerKeys(Player player) {
        if(hasKeys(player)) return;

        if(getDatabase().getCollection("player_keys").countDocuments() == 0) {
            getDatabase().createCollection("player_keys");
        }

        final MongoCollection<Document> collection = getKeyCollection();

        Document document = new Document("uuid", player.getUniqueId().toString());

        for (KeyTypes k : KeyTypes.values()) {
            document.append(k.name().toLowerCase(), 0);
        }

        collection.insertOne(document);
    }

    private boolean hasKeys(Player player) {
        return getKeyCollection().find(new Document("uuid", player.getUniqueId().toString())).first() != null;
    }

    public void update(Document newDocument,Player player) {
        if(!hasKeys(player)) return;
        Document keys = getKeys(player);
        getKeyCollection().findOneAndReplace(keys,newDocument);
    }

    // Add 1 key to the player
    public void addKey(Player player, KeyTypes type) {
        Document document = getKeys(player);
        document.replace(type.name().toLowerCase(), document.getInteger(type.name().toLowerCase()) + 1);
        update(document,player);
    }

    // Add multiple keys to the player
    public void addKey(Player player, KeyTypes type, int amount) {
        Document document = getKeys(player);
        document.replace(type.name().toLowerCase(), document.getInteger(type.name().toLowerCase()) + amount);
        update(document,player);
    }

    // Remove a key from the player keys account.
    public void removeKey(Player player, KeyTypes type) {
        Document document = getKeys(player);
        if(document.getInteger(type.name().toLowerCase()) - 1 < 0) return;
        document.replace(type.name().toLowerCase(), document.getInteger(type.name().toLowerCase()) - 1);
        update(document,player);
    }

    // Remove multiple keys from the player keys account.
    public void removeKey(Player player, KeyTypes type,int amount) {
        Document document = getKeys(player);
        if(document.getInteger(type.name().toLowerCase()) - amount < 0) return;
        document.replace(type.name().toLowerCase(), document.getInteger(type.name().toLowerCase()) - amount);
        update(document,player);
    }

    // Set the player keys account to a specific amount.
    public void setKey(Player player, KeyTypes type, int amount) {
        Document document = getKeys(player);
        if(amount < 0) return;
        document.replace(type.name().toLowerCase(), amount);
        update(document,player);
    }

    // Reseet the player keys account to 0.
    public void resetKeys(Player player) {
        Document document = getKeys(player);
        for (KeyTypes k : KeyTypes.values()) {
            document.replace(k.name().toLowerCase(), 0);
        }
        update(document,player);
    }

    // Get player keys account.
    public Document getKeys(Player player) {
        return getKeyCollection().find(new Document("uuid", player.getUniqueId().toString())).first();
    }


}
