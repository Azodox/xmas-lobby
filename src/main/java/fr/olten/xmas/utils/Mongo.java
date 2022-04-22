package fr.olten.xmas.utils;

import com.mongodb.*;
import fr.olten.xmas.Lobby;

public class Mongo {

    private final MongoClient mongoClient;

    public Mongo(Lobby main) {
        MongoCredential credential = MongoCredential.createCredential(
                main.getConfig().getString("mongodb.username"),
                main.getConfig().getString("mongodb.authDatabase"),
                main.getConfig().getString("mongodb.password").toCharArray()
        );

        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(10)
                .connectTimeout(100000)
                .maxWaitTime(100000)
                .socketTimeout(1000)
                .heartbeatConnectTimeout(600000)
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .build();

        this.mongoClient = new MongoClient(
                new ServerAddress(
                        main.getConfig().getString("mongodb.host"),
                        main.getConfig().getInt("mongodb.port")),
                credential, options
        );
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}