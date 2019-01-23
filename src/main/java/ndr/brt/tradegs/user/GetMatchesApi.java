package ndr.brt.tradegs.user;

import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import ndr.brt.tradegs.ApiEndpoint;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.match.Match;
import ndr.brt.tradegs.match.Matches;

import java.util.List;

public class GetMatchesApi implements ApiEndpoint {
    private Router router;
    private Matches matches;

    public GetMatchesApi(Router router, Matches matches) {
        this.router = router;
        this.matches = matches;
    }

    @Override
    public void run() {
        final Handler<RoutingContext> handler = context -> {
            List<Match> matches = this.matches.get(context.request().getParam("userId"));
            context.response()
                    .setStatusCode(200)
                    .putHeader("Content-type", "application/json")
                    .end(Json.toJson(matches));
        };

        router.get("/matches/:userId").handler(handler);
    }
}
