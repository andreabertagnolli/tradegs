package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.DiscogsClient;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiscogsClientTest {

    @Disabled
    @Test
    void check_inventory_and_handle_pagination() {
        DiscogsClient client = new DiscogsClient();

        List<Listing> listings = client.inventory("smellymilk");

        assertThat(listings).size().isGreaterThan(50);
    }

    @Disabled
    @Test
    void check_wantlist_and_handle_pagination() {
        DiscogsClient client = new DiscogsClient();

        List<Want> wants = client.wantlist("smellymilk");

        assertThat(wants).size().isGreaterThan(50);
    }
}