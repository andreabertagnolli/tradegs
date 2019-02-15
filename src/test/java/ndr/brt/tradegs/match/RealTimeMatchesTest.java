package ndr.brt.tradegs.match;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Release;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.inventory.Inventories;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import ndr.brt.tradegs.wantlist.Wantlists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RealTimeMatchesTest {

    private RealTimeMatches matches;
    private Wantlists wantlists = mock(Wantlists.class);
    private Inventories inventories = mock(Inventories.class);
    private Users users = mock(Users.class);

    @BeforeEach
    void setUp() {
        matches = new RealTimeMatches(users, wantlists, inventories);
    }

    @Test
    void no_match() {
        when(wantlists.get(any())).thenReturn(emptyList());
        when(inventories.get(any())).thenReturn(emptyList());
        when(users.stream()).thenReturn(Stream.of(user("user"), user("dickie"), user("lydia")));

        List<Match> matches = this.matches.get("user");

        assertThat(matches).isEmpty();
    }

    @Test
    void simple_match() {
        when(wantlists.get("user")).thenReturn(singletonList(new Want(2345)));
        when(inventories.get("user")).thenReturn(singletonList(new Listing(987654, new Release(12345))));
        when(users.stream()).thenReturn(Stream.of(user("user"), user("frankie"), user("jodie")));
        when(wantlists.get("frankie")).thenReturn(singletonList(new Want(12345)));
        when(wantlists.get("jodie")).thenReturn(emptyList());
        when(inventories.get("frankie")).thenReturn(singletonList(new Listing(74364, new Release(2345))));
        when(inventories.get("jodie")).thenReturn(singletonList(new Listing(58652543, new Release(7765))));

        List<Match> matches = this.matches.get("user");

        assertThat(matches).hasSize(1);
    }

    private User user(String id) {
        return new User().created(id);
    }
}