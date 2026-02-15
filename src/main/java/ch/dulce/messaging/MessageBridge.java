package ch.dulce.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MessageBridge extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("amq:queue:{{amq.queue}}")
                .id("ibmmq-amq")
                .transacted()
                .log("Sending message from Artemis to IBMMQ: ${body}")
                .to("ibmmq:queue:{{ibm.mq.queue}}");

    }
}
