package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.cqrs.query.storage.HeroesStorageService;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

@Component
public class UpdateHeroEventHandler implements EventHandler<HeroEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateHeroEventHandler.class);
    private final HeroesStorageService heroesStorageService;

    @Autowired
    public UpdateHeroEventHandler(HeroesStorageService heroesStorageService) {
        this.heroesStorageService = heroesStorageService;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle update hero event...");
        // TODO: update data in the storage
    }
}
