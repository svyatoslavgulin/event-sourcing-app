package ru.ardecs.sideprojects.eventsourcing.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * @author <a href="mailto:sergey.ilminskih@ardecs.com">Sergey Ilminskih</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeroEntity {
    private String id;
    private String name;
    private Date createdDate;

    public HeroEntity() {
    }

    public HeroEntity(String id, String name) {
        this.id = id;
        this.name = name;
        this.createdDate = new Date();
    }

    @Id
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
