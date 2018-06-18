package ndr.brt.tradegs;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public enum MongoDbConnection {
    MongoDbConnection;

    private final MongoClient client;

    MongoDbConnection() {
        client = new MongoClient("localhost", 12345);
    }

    public static MongoDatabase database() {
        return MongoDbConnection.client.getDatabase("test");
    }
}
