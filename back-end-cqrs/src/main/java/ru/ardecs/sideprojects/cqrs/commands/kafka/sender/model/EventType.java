package ru.ardecs.sideprojects.cqrs.commands.kafka.sender.model;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
public enum EventType {
    UPDATE,
    CREATE,
    REMOVE;
}
