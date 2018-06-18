package ndr.brt.tradegs.inventory;

import com.google.gson.reflect.TypeToken;
import com.mongodb.Function;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.MongoDbConnection;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DbInventories implements Inventories {

    private static final Type documentList = new TypeToken<List<Document>>(){}.getType();
    private final MongoCollection<Document> inventories;

    public DbInventories() {
        MongoDatabase database = MongoDbConnection.database();
        database.createCollection("inventories");
        inventories = database.getCollection("inventories");
    }

    @Override
    public void save(String id, List<Listing> listings) {
        Optional.of(Map.of("id", id, "listings", listings))
                .map(Json::toJson)
                .map(Document::parse)
                .ifPresent(inventories::insertOne);
    }

    public List<Listing> get(String id) {
        return null;
        /*return inventories.find(new Document("id", id))
                .map(it -> it.get("listings", List.class))
                .map(it -> it.stream()
                        .map(Document.class::cast)
                        .map(Document::toJson)
                        .map(ti -> Json.fromJson(ti, Listing.class))
                        .collect(toList()))
                .first();*/
    }
}
