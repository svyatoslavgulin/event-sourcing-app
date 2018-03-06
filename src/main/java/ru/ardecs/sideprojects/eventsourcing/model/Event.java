package ru.ardecs.sideprojects.eventsourcing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@Document(collection = "event")
public class Event {

    @Id
    private String id;
    @Indexed
    private String objectId;
    @Indexed
    private EventType type;
    @Indexed
    private String name;

    private String payload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

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
