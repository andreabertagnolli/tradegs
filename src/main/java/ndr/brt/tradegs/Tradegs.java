package ndr.brt.tradegs;

import spark.servlet.SparkApplication;

import static spark.Spark.post;

public class Tradegs implements SparkApplication {

    public void init() {
        post("/users", (req, res) -> 200);
    }

}
