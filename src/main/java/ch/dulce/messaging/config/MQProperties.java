package ch.dulce.messaging.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "ibmmq")
public interface MQProperties {

    List<Bridge> sourceBridges();

    List<Bridge> targetBridges();

    String host();

    int port();

    String channel();

    String queueManager();

    String username();

    String password();

    interface Bridge {
        String sourceQueue();

        String targetQueue();
    }
}
