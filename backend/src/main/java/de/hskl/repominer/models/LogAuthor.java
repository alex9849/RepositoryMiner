package de.hskl.repominer.models;

public class LogAuthor {


    private int projectId;
    private int id;
    private String name;

    public LogAuthor(int projectId, int id, String name) {
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

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
