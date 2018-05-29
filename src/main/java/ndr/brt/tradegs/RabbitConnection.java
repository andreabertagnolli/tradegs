package ndr.brt.tradegs;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public enum RabbitConnection implements Supplier<Connection> {

    RABBIT_CONNECTION;

    private final Connection connection;

    RabbitConnection() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connection = connectionFactory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection rabbitConnection() {
        return RABBIT_CONNECTION.get();
    }

    @Override
    public Connection get() {
        return connection;
    }
}
