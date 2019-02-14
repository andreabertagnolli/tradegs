package ndr.brt.tradegs.user;

import com.mongodb.Function;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ndr.brt.tradegs.*;
import org.bson.Document;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DbUsers implements Users {

    private final MongoCollection<Document> users;
    private final MongoCollection<Document> usersSnapshot;
    private final Bus events;

    public DbUsers(Bus events) {
        MongoDatabase database = MongoDbConnection.mongoDatabase();
        this.users = database.getCollection("users");
        this.usersSnapshot = database.getCollection("usersSnapshot");
        this.events = events;
    }

    @Override
    public void save(User user) {
        user.changes().forEach(change -> {
            users.insertOne(Document.parse(Json.toJson(change)));
            events.publish(change);
        });

        usersSnapshot.insertOne(Document.parse(Json.toJson(user)));
        user.clearChanges();
    }

    @Override
    public User get(String id) {
        User user = new User();

        users.find(new Document("id", id)).forEach((Consumer<Document>) it -> {
                Class<? extends Event> clazz = EventClasses.get(it.get("type", String.class));
                Event event = Json.fromJson(it.toJson(), clazz);
                user.apply(event);
            });

        return user;
    }

    @Override
    public List<String> except(String user) {
        Spliterator<User> spliterator = users.find()
                .map(it -> it.get("id", String.class))
                .map(it -> new User().created(it)).spliterator();

        return StreamSupport.stream(spliterator, false)
                .filter(it -> !user.equals(it.id()))
                .map(User::id)
                .collect(Collectors.toList());
    }

    public Stream<User> stream() {
        Spliterator<User> spliterator = usersSnapshot.find()
                .map(Document::toJson)
                .map(it -> Json.fromJson(it, User.class))
                .spliterator();
        
        return StreamSupport.stream(spliterator, false);
    }
}
