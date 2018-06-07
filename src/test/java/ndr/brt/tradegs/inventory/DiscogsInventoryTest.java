package ndr.brt.tradegs.inventory;

import ndr.brt.tradegs.discogs.Discogs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiscogsInventoryTest {

    private final Discogs discogs = mock(Discogs.class);
    private final Inventories inventories = mock(Inventories.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);

    @Test
    void fetch_listings_from_discogs_persist_it_and_return_the_key() {
        List<Listing> listings = asList(
                new Listing(), new Listing()
        );
        when(discogs.inventory("utente")).thenReturn(listings);
        when(idGenerator.generate()).thenReturn("idInventory");

        DiscogsInventory inventory = new DiscogsInventory(discogs, idGenerator, inventories);

        String result = inventory.fetch("utente");

        assertThat(result, is("idInventory"));
        verify(inventories).save("idInventory", listings);
    }
}