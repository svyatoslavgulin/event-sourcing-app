package ru.ardecs.sideprojects.cqrs.commands.kafka.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HeroesEventSenderService {
    private final KafkaSenderService kafkaSenderService;

    @Value("${app.topic.hero-events}")
    private String topic;

    @Autowired
    public HeroesEventSenderService(KafkaSenderService kafkaSenderService) {
        this.kafkaSenderService = kafkaSenderService;
    }

    public void send(String message) {
        kafkaSenderService.send(topic, message);
    }
}
