package ru.ardecs.sideprojects.eventsourcing.service;

import org.springframework.stereotype.Component;
import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Component
public class TemporaryStoreService {

    private final ConcurrentLinkedQueue<Event> store = new ConcurrentLinkedQueue<>();

    public void save(Event event) {
        store.add(event);
    }

    public List<Event> getEvents(EventType type, String name) {
        return store.stream()
                .filter(event -> event.getName().equalsIgnoreCase(name))
                .filter(event -> event.getType().equals(type))
                .collect(Collectors.toList());
    }

    public List<Event> getEventsByName(String name) {
        return store.stream()
                .filter(event -> event.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
