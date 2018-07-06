package ndr.brt.tradegs.discogs.api;

import java.util.List;

public class WantlistPage extends Page {
    private final List<Want> wants;

    protected WantlistPage(Pagination pagination, List<Want> wants) {
        super(pagination);
        this.wants = wants;
    }

    public List<Want> wants() {
        return wants;
    }
}
