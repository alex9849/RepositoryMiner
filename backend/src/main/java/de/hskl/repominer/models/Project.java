package de.hskl.repominer.models;

import java.util.Date;

public class Project {
    private final Integer id;
    public Date lastUpdate;

    public Project() {
        this.id = null;
    }

    public Project(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
