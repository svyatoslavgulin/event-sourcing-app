package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.kafka.reciever.ErrorHeroEventSender;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

import java.util.Date;
import java.util.Optional;

@Component
public class UpdateHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateHeroEventHandler.class);
    private final HeroQueryRepository repository;
    private final ErrorHeroEventSender errorHeroEventSender;

    @Autowired
    public UpdateHeroEventHandler(HeroQueryRepository repository, ErrorHeroEventSender errorHeroEventSender) {
        this.repository = repository;
        this.errorHeroEventSender = errorHeroEventSender;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle update hero event...");
        Optional<HeroQueryEntity> heroQueryEntity = repository.findById(event.getObjectId());
        heroQueryEntity.ifPresent(entity -> {
            entity.setName(event.getPayload().get("name"));
            entity.setLastEventId(event.getId());
            entity.setLastEventDate(new Date());
            try {
                repository.save(entity);
            } catch (Exception e) {
                try {
                    errorHeroEventSender.sendErrorEvent(event, e.getMessage());
                } catch (JsonProcessingException e1) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }
}
