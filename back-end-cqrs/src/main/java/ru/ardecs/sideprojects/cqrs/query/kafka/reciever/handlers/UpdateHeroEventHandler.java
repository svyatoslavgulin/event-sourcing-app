package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

import java.util.Optional;

@Component
public class UpdateHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateHeroEventHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final HeroQueryRepository repository;

    @Autowired
    public UpdateHeroEventHandler(HeroQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle update hero event...");
        Optional<HeroQueryEntity> heroQueryEntity = repository.findById(event.getObjectId());
        heroQueryEntity.ifPresent(e -> {
            e.setName(event.getPayload().get("name"));
            // TODO: тут могут быть ошибки сохранения (нарушение констрейнтов)
            repository.save(e);
        });
    }
}
