package ru.ardecs.sideprojects.cqrs.query.kafka.reciever.handlers;

import ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model.Event;

public interface EventHandler<T> {

    void apply(Event event);
}
