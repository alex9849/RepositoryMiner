package de.hskl.repominer.models;

public class CurrentPath {

    private int fileId;
    private String hash;
    private String path;

    public CurrentPath(int fileId, String hash, String path){
        this.fileId = fileId;
        this.hash = hash;
        this.path = path;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
