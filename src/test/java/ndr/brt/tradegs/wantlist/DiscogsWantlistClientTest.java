package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.inventory.IdGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class DiscogsWantlistClientTest {

    private final Discogs discogs = mock(Discogs.class);
    private final Wantlists wantlists = mock(Wantlists.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);

    @Test
    void fetch_wants_from_discogs_persist_them_and_return_the_key() {
        List<Want> wants = asList(
                new Want(12), new Want(13)
        );
        when(discogs.wantlist("utente")).thenReturn(wants);
        when(idGenerator.generate()).thenReturn("idWantlist");

        DiscogsWantlistClient wantlist = new DiscogsWantlistClient(discogs, idGenerator, wantlists);

        String result = wantlist.fetch("utente");

        assertThat(result, is("idWantlist"));
        verify(wantlists).save("idWantlist", wants);
    }

}