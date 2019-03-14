package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import ndr.brt.tradegs.discogs.api.Page;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;

public class Pages<T extends Page> {

    private final Logger log = getLogger(getClass());
    private final GetPage<T> getPage;

    public Pages(GetPage<T> getPage) {
        this.getPage = getPage;
    }

    public Future<List<T>> getFor(String userId) {
        Future<List<T>> result = Future.future();

        getPage.apply(userId, 1).setHandler(async -> {
            if (async.succeeded()) {
                T firstPage = async.result();
                int totalPages = firstPage.pages();
                log.info("Total pages: {}", totalPages);

                List<Future> futures = IntStream
                        .range(2, totalPages + 1)
                        .mapToObj(page -> getPage.apply(userId, page))
                        .collect(Collectors.toList());

                CompositeFuture.all(futures).setHandler(joinPages(result, firstPage));
            }
        });

        return result;
    }

    private Handler<AsyncResult<CompositeFuture>> joinPages(Future<List<T>> future, T firstPage) {
        return async -> {
            if (async.succeeded()) {
                CompositeFuture remnants = async.result();
                Collection remnantPages = IntStream
                        .range(0, remnants.size())
                        .mapToObj(remnants::resultAt)
                        .map(Page.class::cast)
                        .collect(Collectors.toList());

                List<T> total = new ArrayList<>();
                total.add(firstPage);
                total.addAll(remnantPages);

                future.complete(total);
            } else {
                future.fail(async.cause());
            }
        };
    }

}
