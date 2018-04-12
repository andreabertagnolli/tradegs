package ndr.brt.tradegs;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;

public enum DbUsers implements Users {

    DbUsers;

    private final MongoDatabase db;

    DbUsers() {
        MongoClient mongo = new MongoClient("localhost", 12345);
        db = mongo.getDatabase("test");
        db.createCollection("users", new CreateCollectionOptions());
    }

    @Override
    public void save(User user) {
        db.getCollection("users").insertOne(Document.parse(Json.toJson(user)));
    }

    @Override
    public User get(String id) {
        return Json.fromJson(db.getCollection("users").find().first().toJson(), User.class);
    }
}
