package de.hskl.repominer.models;

public class File {
    private final int id;
    private final int projectId;

    public File(int id, int projectId) {
        this.id = id;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return projectId;
    }
}
