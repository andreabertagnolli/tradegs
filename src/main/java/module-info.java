module ndr.brt.tradegs {

    requires jdk.incubator.httpclient;
    requires org.apache.commons.lang3;
    requires spark.core;
    requires mongo.java.driver;
    requires com.rabbitmq.client;
    requires gson;
    requires slf4j.api;

    exports ndr.brt.tradegs;
    exports ndr.brt.tradegs.discogs;
    exports ndr.brt.tradegs.inventory;
    exports ndr.brt.tradegs.user;
    exports ndr.brt.tradegs.wantlist;
}