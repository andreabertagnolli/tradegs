package ndr.brt.tradegs.user;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import ndr.brt.tradegs.Commands;
import ndr.brt.tradegs.Json;
import ndr.brt.tradegs.RabbitConnection;
import ndr.brt.tradegs.wantlist.FetchWantlist;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static com.rabbitmq.client.BuiltinExchangeType.TOPIC;
import static java.util.Arrays.asList;

public class UserCreatedListener implements Runnable {

    private final Commands commands;
    private final Channel channel;

    public UserCreatedListener(Commands commands) {
        this.commands = commands;
        try {
            channel = RabbitConnection.rabbitConnection().createChannel();
            channel.exchangeDeclare("tradegs", TOPIC);
            channel.queueDeclare("startFetch", true, true, false, null);
            channel.queueBind("startFetch", "tradegs", "user.created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            channel.basicConsume("startFetch", true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    Optional.of(body)
                            .map(String::new)
                            .map(it -> Json.fromJson(it, UserCreated.class))
                            .map(UserCreated::id)
                            .map(it -> asList(new FetchInventory(it), new FetchWantlist(it)))
                            .ifPresent(it -> it.forEach(commands::post));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
