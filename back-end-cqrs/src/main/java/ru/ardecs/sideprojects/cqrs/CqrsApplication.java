package ru.ardecs.sideprojects.cqrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ardecs.sideprojects.cqrs.commands.controller.ObjectUtils;
import ru.ardecs.sideprojects.cqrs.commands.controller.model.HeroCommand;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.HeroesEventSenderService;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.EventType;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.ardecs.sideprojects.cqrs.query.storage.repository"})
public class CqrsApplication implements CommandLineRunner {

    @Autowired
    private HeroesEventSenderService sender;

    public static void main(String[] args) {
        SpringApplication.run(CqrsApplication.class, args);
    }

    @Override
    public void run(String... strings) throws JsonProcessingException {

        HeroCommand command = new HeroCommand("123", "test");

        Event event = new Event();
        event.setType(EventType.CREATE);
        event.setName(HeroCommand.class.getSimpleName());
        event.setObjectId(UUID.randomUUID().toString());
        event.setPayload(ObjectUtils.getPropertiesMap(command));
        event.setCreatedDate(new Date());

        sender.send(new ObjectMapper().writeValueAsString(event));
    }
}