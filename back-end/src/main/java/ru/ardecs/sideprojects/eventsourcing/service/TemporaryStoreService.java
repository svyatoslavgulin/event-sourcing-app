package ru.ardecs.sideprojects.eventsourcing.service;

import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.eventsourcing.model.HeroEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class TemporaryStoreService {

    private final ConcurrentLinkedQueue<HeroEntity> store = new ConcurrentLinkedQueue<>();

    public TemporaryStoreService() {
        store.add(new HeroEntity("1", "Sergey"));
        store.add(new HeroEntity("2", "Slava"));
        store.add(new HeroEntity("3", "Super Men"));
    }

    public void save(HeroEntity entity) {
        store.add(entity);
    }

    public HeroEntity getEntity(String name) {
        return store.stream()
                .filter(entity -> entity.getName().equalsIgnoreCase(name))
                .findFirst().orElseGet(null);
    }

    public HeroEntity getEntityById(String id) {
        return store.stream()
                .filter(entity -> entity.getId().equalsIgnoreCase(id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public List<HeroEntity> getEntitys() {
        return store.stream()
                .collect(Collectors.toList());
    }

    public HeroEntity update(HeroEntity entity) {
        HeroEntity heroEntity = store.stream()
                .filter(e -> e.getId().equalsIgnoreCase(entity.getId()))
                .findFirst().get();

        heroEntity.setName(entity.getName());

        return heroEntity;
    }

    public void delete(String id) {
        store.remove(
                store.stream().filter(entity -> entity.getId().equalsIgnoreCase(id)).findFirst().get()
        );
    }
}
