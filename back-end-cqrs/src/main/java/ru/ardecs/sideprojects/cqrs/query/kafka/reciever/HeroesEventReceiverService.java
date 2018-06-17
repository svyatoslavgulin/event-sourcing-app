package ru.ardecs.sideprojects.cqrs.query.kafka.reciever;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers.CreateHeroEventHandler;
import ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers.DeleteHeroEventHandler;
import ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers.ErrorHeroEventHandler;
import ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers.UpdateHeroEventHandler;

@Service
public class HeroesEventReceiverService {
    private static final Logger LOG = LoggerFactory.getLogger(HeroesEventReceiverService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CreateHeroEventHandler createHeroEventHandler;
    private final DeleteHeroEventHandler deleteHeroEventHandler;
    private final UpdateHeroEventHandler updateHeroEventHandler;
    private final ErrorHeroEventHandler errorHeroEventHandler;

    @Autowired
    public HeroesEventReceiverService(CreateHeroEventHandler createHeroEventHandler,
                                      DeleteHeroEventHandler deleteHeroEventHandler,
                                      UpdateHeroEventHandler updateHeroEventHandler,
                                      ErrorHeroEventHandler errorHeroEventHandler) {
        this.createHeroEventHandler = createHeroEventHandler;
        this.deleteHeroEventHandler = deleteHeroEventHandler;
        this.updateHeroEventHandler = updateHeroEventHandler;
        this.errorHeroEventHandler = errorHeroEventHandler;
    }

    @KafkaListener(topics = "${app.topic.hero-events}")
    public void receive(@Payload String message,
                        @Headers MessageHeaders headers) {
        LOG.info("received message='{}'", message);

        try {
            Event event = MAPPER.readValue(message, Event.class);
            switch (event.getType()) {
                case CREATE:
                    createHeroEventHandler.apply(event);
                    break;
                case UPDATE:
                    updateHeroEventHandler.apply(event);
                    break;
                case REMOVE:
                    deleteHeroEventHandler.apply(event);
                    break;
                case ERROR:
                    errorHeroEventHandler.apply(event);
                    break;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}