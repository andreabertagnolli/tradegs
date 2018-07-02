package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.api.Listing;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiscogsClientTest {

    @Test
    void check_inventory_and_handle_pagination() {
        DiscogsClient client = new DiscogsClient();

        List<Listing> listings = client.inventory("smellymilk");

        assertThat(listings).size().isGreaterThan(50);
    }
}