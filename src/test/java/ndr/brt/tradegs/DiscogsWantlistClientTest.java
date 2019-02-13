package ndr.brt.tradegs;

import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.discogs.Discogs;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.inventory.IdGenerator;
import ndr.brt.tradegs.wantlist.DiscogsWantlistClient;
import ndr.brt.tradegs.wantlist.Wantlists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
class DiscogsWantlistClientTest {

    private final Discogs discogs = mock(Discogs.class);
    private final Wantlists wantlists = mock(Wantlists.class);
    private final IdGenerator idGenerator = mock(IdGenerator.class);

    @Test
    void fetch_wants_from_discogs_persist_them_and_return_the_key(VertxTestContext context) {
        List<Want> wants = asList(
                new Want(12), new Want(13)
        );
        when(discogs.wantlist("utente")).thenReturn(Future.succeededFuture(wants));
        when(idGenerator.generate()).thenReturn("idWantlist");

        DiscogsWantlistClient wantlist = new DiscogsWantlistClient(discogs, idGenerator, wantlists);

        wantlist.fetch("utente").setHandler(async -> {
            assertThat(async.result(), is("idWantlist"));
            verify(wantlists).save("utente", wants);
            context.completeNow();
        });

    }

}