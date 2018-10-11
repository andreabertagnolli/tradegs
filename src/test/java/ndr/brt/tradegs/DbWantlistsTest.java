package ndr.brt.tradegs;

import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.wantlist.DbWantlists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DbWantlistsTest {

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
        DbWantlists wantlists = new DbWantlists();
        String id = IdGenerator.uuidGenerator().generate();

        wantlists.save(id, asList(new Want(150899904)));

        List<Want> wants = wantlists.get(id);
        assertThat(wants, hasSize(1));
        assertEquals(150899904, wants.get(0).id());
    }

}