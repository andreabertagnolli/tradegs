package ndr.brt.tradegs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public enum MongoDbConnection {
    MongoDbConnection;

    private final ResourceBundle properties = ResourceBundle.getBundle("mongodb");
    private final MongoClient client;
    private final String host;
    private final Integer port;

    MongoDbConnection() {
        host = properties.getString("host");
        port = Integer.valueOf(properties.getString("port"));
        MongoCredential mongoCredential = MongoCredential.createCredential("test", "test", "test".toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, port);
        client = new MongoClient(serverAddress, mongoCredential, MongoClientOptions.builder().build());
        client.listDatabases().forEach((Consumer<? super Document>) it -> System.out.println(it.toString()));
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
