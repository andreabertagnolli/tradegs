package ndr.brt.tradegs.wantlist;

import ndr.brt.tradegs.discogs.api.Want;

import java.util.List;

public class Wantlist {
    private String id;
    private List<Want> wants;

    public List<Want> wants() {
        return wants;
    }
}
