package ndr.brt.tradegs.inventory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.MongoDbConnection;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbInventories implements Inventories {

    private final MongoCollection<Document> inventories;

    DbInventories() {
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
        return inventories.find(new Document("id", id))
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, InventoryData.class))
                .map(InventoryData::listings)
                .first();
    }
}
