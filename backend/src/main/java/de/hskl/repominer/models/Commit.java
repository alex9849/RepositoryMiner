package de.hskl.repominer.models;

import java.util.Date;

public class Commit {
    private final int projectId;
    private final String hash;
    private final int authorId;
    private final Date timeStamp;
    private final String messsage;

    public Commit(int projectId, String hash, int authorId, Date timeStamp, String messsage) {
        this.projectId = projectId;
        this.hash = hash;
        this.authorId = authorId;
        this.timeStamp = timeStamp;
        this.messsage = messsage;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getHash() {
        return hash;
    }

    public int getAuthorId() {
        return authorId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMesssage() {
        return messsage;
    }
}
