package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;
import ru.ardecs.sideprojects.cqrs.query.storage.model.HeroQueryEntity;
import ru.ardecs.sideprojects.cqrs.query.storage.repository.HeroQueryRepository;

@Component
public class CreateHeroEventHandler implements EventHandler<HeroQueryEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(CreateHeroEventHandler.class);
    private final HeroQueryRepository repository;

    @Autowired
    public CreateHeroEventHandler(HeroQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void apply(Event event) {
        LOG.debug("Handle create hero event...");

        HeroQueryEntity heroQueryEntity = new HeroQueryEntity();
        heroQueryEntity.setId(event.getPayload().get("id"));
        heroQueryEntity.setName(event.getPayload().get("name"));

        // TODO: обрабатывать ошибки сохранения (например проверка констрейнтов на уровне БД)
        try {
            repository.save(heroQueryEntity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
