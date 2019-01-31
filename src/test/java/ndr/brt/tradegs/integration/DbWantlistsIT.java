package ndr.brt.tradegs.integration;

import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.wantlist.DbWantlists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DbWantlistsIT {

    private DbWantlists wantlists;

    @BeforeEach
    void setUp() {
        EmbeddedMongoDb.activate();
        wantlists = new DbWantlists();
    }

    @Test
    void store_and_retrieve() {
        String id = "john_peel";

        wantlists.save(id, asList(new Want(150899904)));

        List<Want> wants = wantlists.get(id);
        assertThat(wants).hasSize(1);
        assertEquals(150899904, wants.get(0).id());
    }

    @Test
    void when_user_does_not_have_wantlist_returns_empty_list() {
        assertThat(wantlists.get("johnny_thunders")).isEmpty();
    }
}