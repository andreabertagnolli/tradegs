package ndr.brt.tradegs;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

import static ndr.brt.tradegs.Event.classOf;

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

        users.find(new Document("id", id)).forEach((Block<Document>) it -> {
                Class<? extends Event> clazz = classOf(it.get("type", String.class));
                Event event = Json.fromJson(it.toJson(), clazz);
                user.apply(event);
            });

        return user;
    }

}
