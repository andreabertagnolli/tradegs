package ndr.brt.tradegs.discogs;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import ndr.brt.tradegs.discogs.api.Page;

import java.util.function.BiFunction;

public interface GetPage<T extends Page> {
    Future<T> apply(String userId, Integer pageNumber);
}
