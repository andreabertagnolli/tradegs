package ndr.brt.tradegs;

import org.apache.qpid.server.SystemLauncher;
import org.junit.platform.commons.logging.LoggerFactory;
import org.slf4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.*;

public enum EmbeddedRabbitBroker {

    Instance;

    public static void initialize() {
        LOG.info("Initialize EmbeddedRabbitBroker");
    }

    private static final String INITIAL_CONFIGURATION = "embedded-rabbit-configuration.json";
    private static final Logger LOG = getLogger(EmbeddedRabbitBroker.class);
    private SystemLauncher systemLauncher;

    EmbeddedRabbitBroker() {
        systemLauncher = new SystemLauncher();
        start();
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
