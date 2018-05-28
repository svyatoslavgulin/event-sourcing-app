package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.cqrs.query.storage.HeroesStorageService;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

@Component
public class DeleteHeroEventHandler implements EventHandler<HeroEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteHeroEventHandler.class);

    private final HeroesStorageService heroesStorageService;

    @Autowired
    public DeleteHeroEventHandler(HeroesStorageService heroesStorageService) {
        this.heroesStorageService = heroesStorageService;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle delete hero event...");
        heroesStorageService.delete(event.getObjectId());
    }
}
