package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.EmbeddedMongoDb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DbInventoriesTest {

    @BeforeEach
    void setUp() {
        EmbeddedMongoDb.initialize();
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