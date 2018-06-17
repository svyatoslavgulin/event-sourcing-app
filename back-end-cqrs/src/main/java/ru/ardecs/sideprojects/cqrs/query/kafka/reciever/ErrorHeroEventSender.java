package ru.ardecs.sideprojects.cqrs.query.kafka.reciever;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.HeroesEventSenderService;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.EventType;

import java.util.Date;
import java.util.UUID;

@Component
public class ErrorHeroEventSender {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorHeroEventSender.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HeroesEventSenderService heroesEventSenderService;

    @Autowired
    public ErrorHeroEventSender(HeroesEventSenderService heroesEventSenderService) {
        this.heroesEventSenderService = heroesEventSenderService;
    }

    public void sendErrorEvent(Event createEvent, String errorMessage) throws JsonProcessingException {
        LOG.debug("Try to send error event for object '{}'...", createEvent.getObjectId());
        Event errorEvent = createErrorEvent(createEvent, errorMessage);

        heroesEventSenderService.send(MAPPER.writeValueAsString(errorEvent));
    }

    private Event createErrorEvent(Event createEvent, String errorMessage) {
        Event errorEvent = new Event();
        errorEvent.setType(EventType.ERROR);
        errorEvent.setId(UUID.randomUUID().toString());
        errorEvent.setName(createEvent.getName());
        errorEvent.setObjectId(createEvent.getObjectId());
        errorEvent.setPayload(createEvent.getPayload());
        errorEvent.setCreatedDate(new Date());
        errorEvent.setErrorMessage(errorMessage);

        return errorEvent;
    }

}
