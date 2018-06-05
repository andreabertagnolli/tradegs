package ndr.brt.tradegs;

import org.apache.qpid.server.SystemLauncher;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedRabbitBroker {

    private static final String INITIAL_CONFIGURATION = "embedded-rabbit-configuration.json";
    private SystemLauncher systemLauncher;

    public EmbeddedRabbitBroker() {
        systemLauncher = new SystemLauncher();
    }

    public void start() {
        try {
            systemLauncher.startup(createSystemConfig());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        systemLauncher.shutdown();
    }

    private Map<String, Object> createSystemConfig() {
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfig = getClass().getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);
        return attributes;
    }
}