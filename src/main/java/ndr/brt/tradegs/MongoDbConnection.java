package ndr.brt.tradegs;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.ResourceBundle;

public enum MongoDbConnection {
    MongoDbConnection;

    private final ResourceBundle properties = ResourceBundle.getBundle("mongodb");
    private final MongoClient client;
    private final String host;
    private final Integer port;

    MongoDbConnection() {
        host = properties.getString("host");
        port = Integer.valueOf(properties.getString("port"));
        client = new MongoClient(host, port);
    }

    public static String host() {
        return MongoDbConnection.host;
    }

    public static int port() {
        return MongoDbConnection.port;
    }

    private MongoDatabase database() {
        return client.getDatabase(properties.getString("database"));
    }

    public static MongoDatabase mongoDatabase() {
        return MongoDbConnection.database();
    }
}
