package de.hskl.repominer.models;

public class File {
    private int id;
    private int projectId;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
