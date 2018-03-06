package ru.ardecs.sideprojects.eventsourcing.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ru.ardecs.sideprojects.eventsourcing.model.Event;
import ru.ardecs.sideprojects.eventsourcing.model.EventType;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findAllByObjectIdAndName(String objectId, String name);
}
