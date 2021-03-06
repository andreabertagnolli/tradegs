package ndr.brt.tradegs.match;

import ndr.brt.tradegs.discogs.api.Listing;
import ndr.brt.tradegs.discogs.api.Want;
import ndr.brt.tradegs.inventory.Inventories;
import ndr.brt.tradegs.user.User;
import ndr.brt.tradegs.user.Users;
import ndr.brt.tradegs.wantlist.Wantlists;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RealTimeMatches implements Matches {
    private final Users users;
    private final Wantlists wantlists;
    private final Inventories inventories;

    public RealTimeMatches(Users users, Wantlists wantlists, Inventories inventories) {
        this.users = users;
        this.wantlists = wantlists;
        this.inventories = inventories;
    }

    @Override
    public List<Match> get(User user) {
        List<Match> matches = new ArrayList<>();
        List<Integer> myWantsIds = wantlists.get(user.wantlistId()).stream()
                .map(Want::id)
                .collect(toList());
        List<Listing> myListings = inventories.get(user.inventoryId());
        List<User> users = this.users.stream().filter(it -> !user.equals(it.id())).collect(toList());

        for (User other : users) {
            List<Want> otherWants = wantlists.get(other.wantlistId());
            List<Listing> otherListings = inventories.get(other.inventoryId());

            List<Listing> get = otherListings.stream().filter(listing -> myWantsIds.contains(listing.release().id())).collect(toList());
            List<Listing> give = myListings.stream().filter(listing -> otherWants.stream().map(Want::id).collect(toList()).contains(listing.release().id())).collect(toList());

            if (!get.isEmpty() && !give.isEmpty()) {
                matches.add(new Match(other.id()).give(give).get(get));
            }
        }

        return matches;
    }
}
