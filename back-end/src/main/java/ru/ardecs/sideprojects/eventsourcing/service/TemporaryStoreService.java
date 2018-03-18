package ru.ardecs.sideprojects.eventsourcing.service;

import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;
import ru.ardecs.sideprojects.eventsourcing.model.TestEntity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class TemporaryStoreService {

    private final ConcurrentLinkedQueue<TestEntity> store = new ConcurrentLinkedQueue<>();

    public TemporaryStoreService() {
        store.add(new TestEntity("1", "Sergey"));
        store.add(new TestEntity("2", "Slava"));
        store.add(new TestEntity("3", "Super Men"));
    }

    public void save(TestEntity entity) {
        store.add(entity);
    }

    public TestEntity getEntity(String name) {
        return store.stream()
                .filter(entity -> entity.getName().equalsIgnoreCase(name))
                .findFirst().orElseGet(null);
    }

    public TestEntity getEntityById(String id) {
        return store.stream()
                .filter(entity -> entity.getId().equalsIgnoreCase(id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public List<TestEntity> getEntitys() {
        return store.stream()
                .collect(Collectors.toList());
    }

    public TestEntity update(TestEntity entity) {
        TestEntity testEntity = store.stream()
                .filter(e -> e.getId().equalsIgnoreCase(entity.getId()))
                .findFirst().get();

        testEntity.setName(entity.getName());

        return testEntity;
    }

    public void delete(String id) {
        store.remove(
                store.stream().filter(entity -> entity.getId().equalsIgnoreCase(id)).findFirst().get()
        );
    }
}
