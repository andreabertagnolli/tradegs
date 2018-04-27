package ndr.brt.tradegs;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ndr.brt.tradegs.Event.classOf;
import static ndr.brt.tradegs.Json.fromJson;

public enum DbUsers implements Users {

    DbUsers;

    private final MongoCollection<Document> users;

    DbUsers() {
        MongoClient mongo = new MongoClient("localhost", 12345);
        MongoDatabase db = mongo.getDatabase("test");
        db.createCollection("users", new CreateCollectionOptions());
        users = db.getCollection("users");
    }

    @Override
    public void save(User user) {
        List<Document> changes = user.changes()
                .map(Json::toJson)
                .map(Document::parse)
                .collect(Collectors.toList());

        users.insertMany(changes);
    }

    @Override
    public User get(String id) {
        User user = new User();

        Block<? super Map.Entry<? extends Class<? extends Event>, String>> applyEvent =
                it -> user.apply(Json.fromJson(it.getValue(), it.getKey()));

        users.find(new Document("id", id))
                .map(it -> Map.entry(classOf(it.get("type", String.class)), it.toJson()))
                .forEach(applyEvent);

        return user;
    }

}
