package ru.ardecs.sideprojects.cqrs.commands.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.HeroesEventSenderService;
import ru.ardecs.sideprojects.eventsourcing.handlers.CommonHandler;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:srgeyilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@RestController
@RequestMapping(value = "/cqrs")
public class HeroesCommandController {
    private static final Logger LOG = LoggerFactory.getLogger(HeroesCommandController.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HeroesEventSenderService senderService;
    private final CommonHandler<HeroEntity> commonHandler;

    @Autowired
    public HeroesCommandController(HeroesEventSenderService senderService, CommonHandler<HeroEntity> commonHandler) {
        this.senderService = senderService;
        this.commonHandler = commonHandler;
    }

    @PostMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void saveHero(@RequestBody HeroEntity body) throws JsonProcessingException {

        Event event = new Event();
        event.setType(EventType.CREATE);
        event.setName(HeroEntity.class.getSimpleName());
        event.setObjectId(UUID.randomUUID().toString());
        event.setPayload(commonHandler.getPropertiesMap(body));
        event.setCreatedDate(new Date());

        senderService.send(MAPPER.writeValueAsString(event));
    }

    @PutMapping("/heroes")
    @CrossOrigin(origins = "http://localhost:4200")
    public void updateHero(@RequestBody HeroEntity body) throws JsonProcessingException {
        LOG.info("Body id: " + body.getId());
        LOG.info("Body name: " + body.getName());

        Event event = new Event();
        event.setType(EventType.UPDATE);
        event.setName(HeroEntity.class.getSimpleName());
        event.setObjectId(body.getId());
        event.setPayload(commonHandler.getPropertiesMap(body));
        event.setCreatedDate(new Date());

        senderService.send(MAPPER.writeValueAsString(event));
    }

    @DeleteMapping("/heroes/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public void deleteHero(@PathVariable String id) throws JsonProcessingException {
        Event event = new Event();
        event.setType(EventType.REMOVE);
        event.setName(HeroEntity.class.getSimpleName());
        event.setObjectId(id);
        event.setCreatedDate(new Date());

        senderService.send(MAPPER.writeValueAsString(event));
    }
}
