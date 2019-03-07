package ndr.brt.tradegs.discogs.pagination;

import ndr.brt.tradegs.discogs.api.Page;

import java.util.concurrent.CompletableFuture;

public interface GetPage<T extends Page> {
    CompletableFuture<T> apply(String userId, Integer pageNumber);
}
