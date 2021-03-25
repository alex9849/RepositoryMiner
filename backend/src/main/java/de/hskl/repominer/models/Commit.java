package de.hskl.repominer.models;

import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Commit {
    private final int projectId;
    private final String hash;
    private final int authorId;
    private final Date timeStamp;
    private final String message;
    @JsonIgnore
    private List<FileChange> fileChanges;
    @JsonIgnore
    private Author author;

    public Commit(int projectId, String hash, int authorId, Date timeStamp, String message) {
        this.projectId = projectId;
        this.hash = hash;
        this.authorId = authorId;
        this.timeStamp = timeStamp;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public List<FileChange> getFileChanges() {
        return fileChanges;
    }

    public void setFileChanges(List<FileChange> fileChanges) {
        this.fileChanges = fileChanges;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
