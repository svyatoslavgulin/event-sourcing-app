package ru.ardecs.sideprojects.cqrs.query.storage.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HERO")
public class HeroQueryEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "lastEventId", nullable = false)
    private String lastEventId;

    @Column(name = "lastErrorEventId")
    private String lastErrorEventId;

    @Column(name = "lastEventDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEventDate;

    @Column(name = "lastErrorEventDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastErrorEventDate;

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

    public String getLastEventId() {
        return lastEventId;
    }

    public void setLastEventId(String lastEventId) {
        this.lastEventId = lastEventId;
    }

    public String getLastErrorEventId() {
        return lastErrorEventId;
    }

    public void setLastErrorEventId(String lastErrorEventId) {
        this.lastErrorEventId = lastErrorEventId;
    }

    public Date getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public Date getLastErrorEventDate() {
        return lastErrorEventDate;
    }

    public void setLastErrorEventDate(Date lastErrorEventDate) {
        this.lastErrorEventDate = lastErrorEventDate;
    }
}
