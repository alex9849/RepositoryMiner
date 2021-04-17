package de.hskl.repominer.models;

import java.util.List;

public class Author {
    private int projectId;
    private int id;
    private String name;
    private List<LogAuthor> logAuthors;

    public Author(int projectId, int id, String name) {
        this.projectId = projectId;
        this.id = id;
        this.name = name;
    }

    public List<LogAuthor> getLogAuthors() {
        return logAuthors;
    }

    public void setLogAuthors(List<LogAuthor> logAuthors) {
        this.logAuthors = logAuthors;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
