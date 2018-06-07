package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

@Component
public class DeleteHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteHeroEventHandler.class);
    private final HeroQueryRepository repository;

    @Autowired
    public DeleteHeroEventHandler(HeroQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle delete hero event...");
        repository.deleteById(event.getObjectId());
    }
}
