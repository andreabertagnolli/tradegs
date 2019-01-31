package ndr.brt.tradegs.wantlist;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.MongoDbConnection;
import ndr.brt.tradegs.discogs.api.Want;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class DbWantlists implements Wantlists {

    private final MongoCollection<Document> wantlists;

    public DbWantlists() {
        MongoDatabase database = MongoDbConnection.mongoDatabase();
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
        return ofNullable(wantlists.find(new Document("id", id))
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, Wantlist.class))
                .map(Wantlist::wants)
                .first())
                .orElse(emptyList());
    }
}
