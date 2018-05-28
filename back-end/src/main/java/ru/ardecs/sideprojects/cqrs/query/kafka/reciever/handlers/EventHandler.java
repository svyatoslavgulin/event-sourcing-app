package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import ru.ardecs.sideprojects.eventsourcing.model.Event;

public interface EventHandler<T> {

    void apply(Event event);
}
