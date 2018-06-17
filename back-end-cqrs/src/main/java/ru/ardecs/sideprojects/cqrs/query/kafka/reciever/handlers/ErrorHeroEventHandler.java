package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

import java.util.Date;
import java.util.Optional;

@Component
public class ErrorHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteHeroEventHandler.class);

    private final HeroQueryRepository repository;

    @Autowired
    public ErrorHeroEventHandler(HeroQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle error hero event...");

        LOG.warn("Operation '{}' was failed for object '{}'. Error message is '{}'",
                event.getType(), event.getObjectId(), event.getErrorMessage());

        Optional<HeroQueryEntity> heroQueryEntity = repository.findById(event.getObjectId());
        heroQueryEntity.ifPresent(entity -> {
            entity.setLastErrorEventId(event.getId());
            entity.setLastErrorEventDate(new Date());
            try {
                repository.save(entity);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        });
    }
}
