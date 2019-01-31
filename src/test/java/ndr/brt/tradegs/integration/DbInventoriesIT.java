package ndr.brt.tradegs.integration;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Release;
import ndr.brt.tradegs.inventory.DbInventories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbInventoriesIT {

    private DbInventories inventories;

    @BeforeEach
    void setUp() {
        EmbeddedMongoDb.activate();
        inventories = new DbInventories();
    }

    @Test
    void store_and_retrieve() {
        String id = "tv_smith";

        inventories.save(id, asList(new Listing(150899904, new Release(1355))));

        List<Listing> listings = inventories.get(id);
        assertThat(listings).hasSize(1);
        assertEquals(150899904, listings.get(0).id());
    }

    @Test
    void when_theres_no_inventories_return_empty_list() {
        assertThat(inventories.get("duncan_redmonds")).isEmpty();
    }
}