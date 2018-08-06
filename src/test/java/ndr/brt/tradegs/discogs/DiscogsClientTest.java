package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiscogsClientTest {

    private DiscogsClient client;

    @BeforeEach
    void setUp() {
        client = new DiscogsClient();
    }

    @Disabled
    @Test
    void check_inventory_and_handle_pagination() {
        List<Listing> listings = client.inventory("smellymilk");

        assertThat(listings).size().isGreaterThan(50);
    }

    @Disabled
    @Test
    void check_wantlist_and_handle_pagination() {
        List<Want> wants = client.wantlist("smellymilk");

        assertThat(wants).size().isGreaterThan(50);
    }
}