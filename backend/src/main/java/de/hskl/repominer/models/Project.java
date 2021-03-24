package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

public class Project {
    private final Integer id;
    private Date lastUpdate;
    @JsonIgnore
    private List<Author> authors;
    @JsonIgnore
    private List<Commit> commits;
    @JsonIgnore
    private List<File> files;

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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
