package ndr.brt.tradegs;

import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.inventory.DiscogsInventoryClient;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.inventory.Inventories;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class DiscogsInventoryClientTest {

    private final Discogs discogs = mock(Discogs.class);
    private final Inventories inventories = mock(Inventories.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);

    @Test
    void fetch_listings_from_discogs_persist_it_and_return_the_key() {
        List<Listing> listings = asList(
                new Listing(12), new Listing(13)
        );
        when(discogs.inventory("utente")).thenReturn(listings);
        when(idGenerator.generate()).thenReturn("idInventory");

        DiscogsInventoryClient inventory = new DiscogsInventoryClient(discogs, idGenerator, inventories);

        String result = inventory.fetch("utente");

        assertThat(result, is("idInventory"));
        verify(inventories).save("idInventory", listings);
    }
}