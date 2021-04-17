package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.List;

public class Project {
    private Integer id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdate;
    @JsonIgnore
    private List<Author> authors;
    @JsonIgnore
    private List<Commit> commits;

    public Project() {
        this.id = null;
    }

    public Project(int id) {
        this.id = id;
    }

    public Project(int id, Date lastUpdate){
        this. id = id;
        this.lastUpdate = lastUpdate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> logAuthors) {
        this.authors = logAuthors;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public void setId(int id) {
        this.id = id;
    }
}
