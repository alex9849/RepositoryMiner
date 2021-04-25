package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.util.List;

public class Commit {
    private int id;
    private int projectId;
    private String hash;
    private boolean isMerge;
    private int authorId;
    private Date timeStamp;
    private String message;
    @JsonIgnore
    private List<FileChange> fileChanges;
    @JsonIgnore
    private LogAuthor logAuthor;

    public Commit(int id, int projectId, String hash, boolean isMerge, int authorId, Date timeStamp, String message) {
        this.id = id;
        this.projectId = projectId;
        this.hash = hash;
        this.isMerge = isMerge;
        this.authorId = authorId;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isMerge() {
        return isMerge;
    }

    public void setMerge(boolean merge) {
        isMerge = merge;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public List<FileChange> getFileChanges() {
        return fileChanges;
    }

    public void setFileChanges(List<FileChange> fileChanges) {
        this.fileChanges = fileChanges;
    }

    public LogAuthor getAuthor() {
        return logAuthor;
    }

    public void setAuthor(LogAuthor logAuthor) {
        this.logAuthor = logAuthor;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
