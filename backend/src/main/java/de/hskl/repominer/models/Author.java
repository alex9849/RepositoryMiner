package de.hskl.repominer.models;

public class Author {
    private final int projectId;
    private final int id;
    private final String name;

    public Author(int projectId, int id, String name) {
        this.projectId = projectId;
        this.id = id;
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
