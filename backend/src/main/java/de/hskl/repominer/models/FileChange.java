package de.hskl.repominer.models;

public class FileChange {
    private int commitId;
    private int fileId;
    private String path;
    private int insertions;
    private int deletions;
    private File file;

    public FileChange(int commitId, int fileId, String path, int insertions, int deletions) {
        this.commitId = commitId;
        this.fileId = fileId;
        this.path = path;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public int getCommitId() {
        return commitId;
    }

    public void setCommitId(int commitId) {
        this.commitId = commitId;
    }

    public int getFileId() {
        return fileId;
    }

    public String getPath() {
        return path;
    }

    public int getInsertions() {
        return insertions;
    }

    public int getDeletions() {
        return deletions;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setInsertions(int insertions) {
        this.insertions = insertions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }
}
