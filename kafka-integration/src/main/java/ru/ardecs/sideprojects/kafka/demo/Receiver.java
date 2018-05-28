package ru.ardecs.sideprojects.kafka.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    @KafkaListener(topics = "${app.topic.foo}")
    public void receive(@Payload String message,
                        @Headers MessageHeaders headers) {
        LOG.info("received message='{}'", message);
        headers.keySet().forEach(key -> LOG.info("{}: {}", key, headers.get(key)));
    }

}