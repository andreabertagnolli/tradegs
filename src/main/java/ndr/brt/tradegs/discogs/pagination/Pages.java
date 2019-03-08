package ndr.brt.tradegs.discogs.pagination;

import ndr.brt.tradegs.discogs.api.Page;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;

public class Pages<T extends Page> {

    private final Logger log = getLogger(getClass());
    private final GetPage<T> getPage;

    public Pages(GetPage<T> getPage) {
        this.getPage = getPage;
    }

    public CompletableFuture<List<T>> getFor(String userId) {
        CompletableFuture<List<T>> future = new CompletableFuture<>();

        getPage.apply(userId, 1).thenAccept(firstPage -> {
            int totalPages = firstPage.pages();
            log.info("Total pages: {}", totalPages);

            List<CompletableFuture<T>> futures = IntStream.range(2, totalPages + 1)
                    .mapToObj(page -> getPage.apply(userId, page))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).thenAccept(it -> {
                List<T> pages = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

                List<T> total = new ArrayList<>();
                total.add(firstPage);
                total.addAll(pages);
                future.complete(total);
            });
        });

        return future;
    }

}
