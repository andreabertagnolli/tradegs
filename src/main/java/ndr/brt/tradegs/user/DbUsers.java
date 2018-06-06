package ndr.brt.tradegs.user;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import ndr.brt.tradegs.Event;
import ndr.brt.tradegs.EventClasses;
import ndr.brt.tradegs.Events;
import ndr.brt.tradegs.Json;
import org.bson.Document;

import java.util.List;

import static java.util.stream.Collectors.toList;

public enum DbUsers implements Users {

    DbUsers;

    private final MongoCollection<Document> users;
    private final Events events;

    DbUsers() {
        MongoClient mongo = new MongoClient("localhost", 12345);
        MongoDatabase db = mongo.getDatabase("test");
        db.createCollection("users", new CreateCollectionOptions());
        users = db.getCollection("users");
        events = Events.events();
    }

    @Override
    public void save(User user) {
        List<String> changes = user.changes()
                .map(Json::toJson)
                .collect(toList());

        changes.forEach(it -> {
            users.insertOne(Document.parse(it));
            events.publish(it);
        });

        user.clearChanges();
    }

    @Override
    public User get(String id) {
        User user = new User();

        users.find(new Document("id", id)).forEach((Block<Document>) it -> {
                Class<? extends Event> clazz = EventClasses.get(it.get("type", String.class));
                Event event = Json.fromJson(it.toJson(), clazz);
                user.apply(event);
            });

        return user;
    }

}
