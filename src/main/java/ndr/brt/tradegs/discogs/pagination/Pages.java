package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import ndr.brt.tradegs.discogs.api.Page;
import ndr.brt.tradegs.discogs.pagination.GetPage;
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
        Future<List<T>> future = Future.future();

        getPage.apply(userId, 1).setHandler(async -> {
            if (async.succeeded()) {
                T first = async.result();
                int totalPages = first.pages();
                log.info("Total pages: {}", totalPages);

                List<Future> futures = IntStream
                        .range(2, totalPages + 1)
                        .mapToObj(page -> getPage.apply(userId, page))
                        .collect(Collectors.toList());

                CompositeFuture.all(futures).setHandler(it -> {
                    if (it.succeeded()) {
                        CompositeFuture result = it.result();
                        Collection pages = IntStream
                                .range(0, result.size())
                                .mapToObj(result::resultAt)
                                .map(Page.class::cast)
                                .collect(Collectors.toList());

                        List<T> total = new ArrayList<>();
                        total.add(first);
                        total.addAll(pages);
                        future.complete(total);
                    } else {
                        future.fail(it.cause());
                    }
                });
            }
        });

        return future;
    }
}
