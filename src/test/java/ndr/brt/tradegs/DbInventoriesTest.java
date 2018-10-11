package ndr.brt.tradegs;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.inventory.DbInventories;
import ndr.brt.tradegs.inventory.IdGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbInventoriesTest {

    private EmbeddedMongoDb mongoDb = new EmbeddedMongoDb();

    @BeforeEach
    void setUp() {
        mongoDb.start();
    }

    @AfterEach
    void tearDown() {
        mongoDb.stop();
    }

    @Test
    void store_and_retrieve() {
        DbInventories inventories = new DbInventories();
        String id = IdGenerator.uuidGenerator().generate();

        inventories.save(id, asList(new Listing(150899904)));

        List<Listing> listings = inventories.get(id);
        assertThat(listings, hasSize(1));
        assertEquals(150899904, listings.get(0).id());
    }
}