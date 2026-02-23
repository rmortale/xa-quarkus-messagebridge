package ch.dulce.messaging;

import ch.dulce.messaging.config.MQProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MessageBridge extends RouteBuilder {


    @Inject
    MQProperties mqProperties;

    @Override
    public void configure() throws Exception {


        for (MQProperties.Bridge sb : mqProperties.sourceBridges()) {
            from("ibmmq:queue:" + sb.sourceQueue() + "?concurrentConsumers=5")
                    .transacted()
                    .log("Sending message from IBMMQ to Artemis: ${body}")
                    .to("amq:queue:" + sb.targetQueue());
        }

        for (MQProperties.Bridge tb : mqProperties.targetBridges()) {
            from("amq:queue:" + tb.sourceQueue() + "?concurrentConsumers=5")
                    .transacted()
                    .log("Sending message from Artemis to IBMMQ: ${body}")
                    .to("ibmmq:queue:" + tb.targetQueue());
        }

    }
}
