package ru.ardecs.sideprojects.eventsourcing.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestEntity {
    private String id;
    private String name;

    public TestEntity() {
    }

    public TestEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
