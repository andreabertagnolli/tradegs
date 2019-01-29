package ndr.brt.tradegs.user;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.*;
import org.bson.Document;

import java.util.List;

public class DbUsers implements Users {

    private final MongoCollection<Document> users;
    private final Bus events;

    public DbUsers(Bus events) {
        MongoDatabase database = MongoDbConnection.mongoDatabase();
        this.users = database.getCollection("users");
        this.events = events;
    }

    @Override
    public void save(User user) {
        user.changes().forEach(change -> {
            users.insertOne(Document.parse(Json.toJson(change)));
            events.publish(change);
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

    @Override
    public List<String> except(String user) {
        // TODO: implement me
        return null;
    }

}
