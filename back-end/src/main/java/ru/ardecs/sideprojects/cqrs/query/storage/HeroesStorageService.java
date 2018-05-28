package ru.ardecs.sideprojects.cqrs.query.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

@Component
public class HeroesStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(HeroesStorageService.class);

    private final Map<String, HeroEntity> ENTITIES = new HashMap<>();

    public List<HeroEntity> getEntities() {
        return new ArrayList<>(ENTITIES.values());
    }

    public HeroEntity getEntityById(String id) {
        return ENTITIES.values().stream()
                .filter(entity -> entity.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }


    public void delete(String entityId) {
        LOG.debug("Try to delete hero with id '{}' from database", entityId);
        ENTITIES.remove(entityId);
        LOG.info("Hero with id '{}' was successfully deleted from database", entityId);
    }
}
