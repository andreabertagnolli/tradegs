package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Page;

import java.util.function.BiFunction;

public interface GetPage<T extends Page> extends BiFunction<String, Integer, T> {
}
