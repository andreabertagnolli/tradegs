package ndr.brt.tradegs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ResourceBundle;

public enum MongoDbConnection {
    MongoDbConnection;

    private final ResourceBundle properties = ResourceBundle.getBundle("mongodb");
    private final MongoClient client;
    private final String host;
    private final Integer port;
    private final String database;

    MongoDbConnection() {
        host = properties.getString("host");
        port = Integer.valueOf(properties.getString("port"));
        database = properties.getString("database");
        String username = properties.getString("username");
        String password = properties.getString("password");
        MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password.toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, port);
        client = new MongoClient(serverAddress, mongoCredential, MongoClientOptions.builder().build());
    }

    public static String host() {
        return MongoDbConnection.host;
    }

    public static int port() {
        return MongoDbConnection.port;
    }

    private MongoDatabase database() {
        return client.getDatabase(database);
    }

    public static MongoDatabase mongoDatabase() {
        return MongoDbConnection.database();
    }
}
