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

@Component
public class CreateHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(CreateHeroEventHandler.class);
    private final HeroQueryRepository repository;
    private final ErrorHeroEventSender errorHeroEventSender;

    @Autowired
    public CreateHeroEventHandler(HeroQueryRepository repository, ErrorHeroEventSender errorHeroEventSender) {
        this.repository = repository;
        this.errorHeroEventSender = errorHeroEventSender;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle create hero event...");

        HeroQueryEntity heroQueryEntity = new HeroQueryEntity();
        heroQueryEntity.setId(event.getObjectId());
        heroQueryEntity.setName(event.getPayload().get("name"));
        heroQueryEntity.setLastEventId(event.getId());
        heroQueryEntity.setLastEventDate(new Date());

        try {
            repository.save(heroQueryEntity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            try {
                errorHeroEventSender.sendErrorEvent(event, e.getMessage());
            } catch (JsonProcessingException e1) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
