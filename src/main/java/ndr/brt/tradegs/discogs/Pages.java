package ndr.brt.tradegs.discogs;

import ndr.brt.tradegs.discogs.api.Page;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Pages<T extends Page> implements Iterable<T> {

    private final GetPage<T> getPage;
    private PageIterator iterator;

    public Pages(GetPage<T> getPage) {
        this.getPage = getPage;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    public Stream<T> getFor(String userId) {
        iterator = new PageIterator(userId);
        return StreamSupport.stream(this.spliterator(), false);
    }

    private class PageIterator implements Iterator<T> {
        private boolean hasNext;
        private int page;
        private final String userId;

        PageIterator(String userId) {
            this.userId = userId;
            this.hasNext = true;
            this.page = 1;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            T current = getPage.apply(userId, page);

            hasNext = current.page() < current.pages();
            page = current.page() + 1;

            return current;
        }
    }
}
