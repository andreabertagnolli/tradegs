package ndr.brt.tradegs.inventory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.MongoDbConnection;
import ndr.brt.tradegs.discogs.api.Listing;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class DbInventories implements Inventories {

    private final MongoCollection<Document> inventories;

    public DbInventories() {
        MongoDatabase database = MongoDbConnection.mongoDatabase();
        inventories = database.getCollection("inventories");
    }

    @Override
    public void save(String id, List<Listing> listings) {
        Optional.of(Map.of("id", id, "listings", listings))
                .map(Json::toJson)
                .map(Document::parse)
                .ifPresent(inventories::insertOne);
    }

    @Override
    public List<Listing> get(String id) {
        return Optional.ofNullable(inventories.find(new Document("id", id))
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, Inventory.class))
                .map(Inventory::listings)
                .first())
                .orElse(emptyList());
    }
}
