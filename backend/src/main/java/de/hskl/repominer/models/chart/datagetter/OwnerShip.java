package de.hskl.repominer.models.chart.datagetter;

public class OwnerShip {
    String authorName;
    int insertions;
    int deletions;

    public int getChangedCode() {
        return insertions + deletions;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getInsertions() {
        return insertions;
    }

    public void setInsertions(int insertions) {
        this.insertions = insertions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }
}