package ndr.brt.tradegs.wantlist;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.MongoDbConnection;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.inventory.Inventory;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DbWantlists implements Wantlists {

    private final MongoCollection<Document> wantlists;

    DbWantlists() {
        MongoDatabase database = MongoDbConnection.database();
        database.createCollection("wantlists");
        wantlists = database.getCollection("wantlists");
    }

    @Override
    public void save(String id, List<Want> wants) {
        Optional.of(Map.of("id", id, "wants", wants))
                .map(Json::toJson)
                .map(Document::parse)
                .ifPresent(wantlists::insertOne);
    }

    public List<Want> get(String id) {
        return wantlists.find(new Document("id", id))
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, Wantlist.class))
                .map(Wantlist::wants)
                .first();
    }
}
