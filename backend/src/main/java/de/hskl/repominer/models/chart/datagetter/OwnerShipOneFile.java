package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.File;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class OwnerShipOneFile {
    private File file;
    private List<OwnerShip> listOfOwners;

    public OwnerShipOneFile() {
        listOfOwners = new ArrayList<>();
    }

    public OwnerShipOneFile(File file) {
        this.file = file;
        listOfOwners = new ArrayList<>();
    }


    public void add(OwnerShip owner){
        listOfOwners.add(owner);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    //gibt ownership fuer entsprechenden author zurueck. falls author nicht vorhanden -> return null
    public OwnerShip getOwnerShipOfAuthor(Author author){
        for(OwnerShip o : listOfOwners){
            if(o.getName().equals(author.getName())){
                return o;
            }
        }

        return null;
    }

    public List<OwnerShip> getListOfOwners() {
        return listOfOwners;
    }

    public void setListOfOwners(List<OwnerShip> listOfOwners) {
        this.listOfOwners = listOfOwners;
    }
}
