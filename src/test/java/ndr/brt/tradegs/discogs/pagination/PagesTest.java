package ndr.brt.tradegs.discogs.pagination;

import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import ndr.brt.tradegs.discogs.api.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(VertxExtension.class)
class PagesTest {

    private GetPage<DummyPage> getPage = mock(GetPage.class);

    @Test
    void get_three_pages(VertxTestContext context) {
        DummyPage pageOne = new DummyPage(3);
        DummyPage pageTwo = new DummyPage(3);
        DummyPage pageThree = new DummyPage(3);
        when(getPage.apply("anyUser", 1)).thenReturn(Future.succeededFuture(pageOne));
        when(getPage.apply("anyUser", 2)).thenReturn(Future.succeededFuture(pageTwo));
        when(getPage.apply("anyUser", 3)).thenReturn(Future.succeededFuture(pageThree));
        Pages<DummyPage> pages = new Pages<>(getPage);

        pages.getFor("anyUser").setHandler(async -> {
            assertThat(async.result()).containsOnly(pageOne, pageTwo, pageThree);
            context.completeNow();
        });
    }


    private static class DummyPage extends Page {

        private int totalPages;

        DummyPage(int totalPages) {
            super(null);
            this.totalPages = totalPages;
        }

        @Override
        public int pages() {
            return totalPages;
        }
    }
}