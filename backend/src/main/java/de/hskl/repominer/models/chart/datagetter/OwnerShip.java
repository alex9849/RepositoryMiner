package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.File;

public class OwnerShip {
    String name;
    File file;
    int insertions;
    int deletions;
    int nrOfChangedLinesInFile;

    public OwnerShip() {

    }

    public OwnerShip(Author author){
        this.name = author.getName();
    }


    public void addOwnerShip(OwnerShip owner){
        if(name == null)
            this.name = owner.getName();

        nrOfChangedLinesInFile += owner.getNrOfChangedLinesInFile();
        addInsertions(owner.getInsertions());
        addDeletions(owner.getDeletions());
    }

    public double getOwnerShipInPercent() {
        Double insert = (double) getInsertions();
        Double del = (double) getDeletions();

        if(getDeletions() + getInsertions() == 0)
            return 0.0;

        System.out.println("insertions: " +  insert + ", deletions: "+ del + ",nrOfLineChagnes = " + nrOfChangedLinesInFile);
        double ownerShipInPercent = ( ( insert +  del ) / nrOfChangedLinesInFile  ) * 100;

        return Math.round(ownerShipInPercent)  ;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void addInsertions(int insertions){
        this.insertions += insertions;
    }

    public void addDeletions(int deletions){
        this.deletions += deletions;
    }

    public void initNrOfLinesChangedInFile(int nrOfChangedLinesInFile){
        if(this.nrOfChangedLinesInFile != 0){
            setNrOfChangedLinesInFile(nrOfChangedLinesInFile);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getNrOfChangedLinesInFile() {
        return nrOfChangedLinesInFile;
    }

    public void setNrOfChangedLinesInFile(int nrOfChangedLinesInFile) {
        this.nrOfChangedLinesInFile = nrOfChangedLinesInFile;
    }
}