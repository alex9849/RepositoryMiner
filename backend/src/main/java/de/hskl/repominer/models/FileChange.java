package de.hskl.repominer.models;

public class FileChange {
    private final String commitHash;
    private final int fileId;
    private final String path;
    private final int insertions;
    private final int deletions;

    public FileChange(String commitHash, int fileId, String path, int insertions, int deletions) {
        this.commitHash = commitHash;
        this.fileId = fileId;
        this.path = path;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public String getCommitHash() {
        return commitHash;
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
}
