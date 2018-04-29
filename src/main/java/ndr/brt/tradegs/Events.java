package ndr.brt.tradegs;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.rabbitmq.client.BuiltinExchangeType.TOPIC;

public interface Events {
    static Events events() {
        return new Instance();
    }

    void publish(String event);

    class Instance implements Events {

        private final Channel channel;

        public Instance() {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            try {
                channel = connectionFactory.newConnection().createChannel();
                channel.exchangeDeclare("tradegs", TOPIC);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void publish(String event) {
            try {
                channel.basicPublish("tradegs", "user.created", null, event.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
