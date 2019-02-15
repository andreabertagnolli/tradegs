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
    private Users users;

    public GetMatchesApi(Router router, Matches matches, Users users) {
        this.router = router;
        this.matches = matches;
        this.users = users;
    }

    @Override
    public void run() {
        final Handler<RoutingContext> handler = context -> {
            String userId = context.request().getParam("userId");
            User user = users.get(userId);
            if (user.exists()) {
                List<Match> matches = this.matches.get(user);
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-type", "application/json")
                        .end(Json.toJson(matches));
            } else {
                context.response()
                        .setStatusCode(404)
                        .end(String.format("User %s does not exists", userId));
            }
        };

        router.get("/matches/:userId").handler(handler);
    }
}
