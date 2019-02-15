package ndr.brt.tradegs.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import ndr.brt.tradegs.*;
import org.bson.Document;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DbUsers implements Users {

    private final MongoCollection<Document> usersEvents;
    private final MongoCollection<Document> usersSnapshot;
    private final Bus events;

    public DbUsers(Bus events) {
        MongoDatabase database = MongoDbConnection.mongoDatabase();
        this.usersEvents = database.getCollection("users_events");
        this.usersSnapshot = database.getCollection("users_snapshot");
        this.events = events;
    }

    @Override
    public void save(User user) {
        user.changes().forEach(change -> {
            usersEvents.insertOne(Document.parse(Json.toJson(change)));
            events.publish(change);
        });

        user.clearChanges();

        usersSnapshot.replaceOne(new Document("id", user.id()), Document.parse(Json.toJson(user)), new ReplaceOptions().upsert(true));
    }

    @Override
    public User get(String id) {
        User user = new User();

        usersEvents.find(new Document("id", id)).forEach((Consumer<Document>) it -> {
                Class<? extends Event> clazz = EventClasses.get(it.get("type", String.class));
                Event event = Json.fromJson(it.toJson(), clazz);
                user.apply(event);
            });

        return user;
    }

    @Override
    public Stream<User> stream() {
        Spliterator<User> spliterator = usersSnapshot.find()
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, User.class))
                .spliterator();

        return StreamSupport.stream(spliterator, false);
    }
}
