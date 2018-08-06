package ndr.brt.tradegs;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public enum RabbitConnection implements Supplier<Connection> {

    RABBIT_CONNECTION;

    private final Connection connection;

    RabbitConnection() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            ResourceBundle properties = ResourceBundle.getBundle("rabbitmq");
            connectionFactory.setHost(properties.getString("host"));
            connectionFactory.setUsername(properties.getString("username"));
            connectionFactory.setPassword(properties.getString("password"));
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
