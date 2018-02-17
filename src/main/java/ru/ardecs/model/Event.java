package ru.ardecs.model;

import ru.ardecs.utils.EventType;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
public class Event {

    private EventType type;
    private String name;
    private String payload;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getType() {

        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
