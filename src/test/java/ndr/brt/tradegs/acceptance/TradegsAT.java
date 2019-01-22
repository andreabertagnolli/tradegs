package ndr.brt.tradegs.acceptance;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import ndr.brt.tradegs.TradegsVerticle;
import ndr.brt.tradegs.integration.EmbeddedMongoDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(VertxExtension.class)
class TradegsAT {

    @BeforeEach
    void setUp(Vertx vertx) throws ExecutionException, InterruptedException {
        EmbeddedMongoDb.activate();

        CompletableFuture<String> future = new CompletableFuture<>();
        vertx.deployVerticle(new TradegsVerticle(12345), it -> {
            if (it.succeeded()) {
                future.complete(it.result());
            } else {
                future.completeExceptionally(it.cause());
            }
        });
        future.get();
    }

    @Test
    @Timeout(5000)
    void name(Vertx vertx) {
        vertx.createHttpClient()
                .post("/users")
                .handler(response -> {
                    response.bodyHandler(body -> {
                        System.out.println("Response: " + body);
                    });
                })
                .end("smellymilk");

    }
}
