package ru.ardecs.sideprojects.cqrs.commands.kafka.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnotherEntityEventSenderService {

    private final KafkaSenderService kafkaSenderService;

    @Value("${app.topic.anotherEntity-events}")
    private String topic;

    @Autowired
    public AnotherEntityEventSenderService(KafkaSenderService kafkaSenderService) {
        this.kafkaSenderService = kafkaSenderService;
    }

    public void send(String message) {
        kafkaSenderService.send(topic, message);
    }
}
