package ndr.brt.tradegs.match;

import java.util.List;

public interface Matches {
    static Matches asRealTime() {
        return new RealTimeMatches();
    }

    List<Match> get(String anUser);
}
