package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.Commit;
import de.hskl.repominer.models.File;
import de.hskl.repominer.models.FileChange;
import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class CodeOwnerShipGetter implements ChartDataGetter {



    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {

        //calc for single file
        List<OwnerShip> ownerShips = calcOwnershipForProject(projectId, projectService);

        for(OwnerShip o : ownerShips){
            System.out.println("OwnerShip for Project: " + projectId + " and Author: " + o.getName() + " is : " + o.getOwnerShipInPercent() + "%");
        }

        return null;
    }

    private List<OwnerShip> calcOwnershipForProject(int projectId,  ProjectService projectService) {
        List<File> fileList = projectService.getFileRepo().loadAllFilesFromProject(projectId);
        List<Author> authorList = projectService.getAuthorRepo().loadAllAuthorsForProject(projectId);
        List<Commit> commitList = projectService.getCommitRepo().loadAllCommitsForProject(projectId);

        List<OwnerShipOneFile> listOfFileOwnerShips = new ArrayList<>();
        List<OwnerShip> ownerList = new ArrayList<>();

        //hole liste aller fileChanges fuer jedes file
        for (File f : fileList) {
            List<FileChange> fileChangeList = projectService.getFileChangeRepo().loadAllFileChangesByFileId(f.getId());
            int nrOfChangedLinesInFile = calcNrOfLinesChangedInFile(f, fileChangeList);

            OwnerShipOneFile ownerShipOfOneFile = new OwnerShipOneFile(f);
            for(Author author: authorList){
                OwnerShip owner = getOwnerShip(author, commitList, fileChangeList, nrOfChangedLinesInFile);
                ownerShipOfOneFile.add(owner);
            }

            listOfFileOwnerShips.add(ownerShipOfOneFile);
        }

        //weise jedem Author die Summer seiner OwnerShips zu
        List<OwnerShip> ownerShips = new ArrayList<>();
        for(Author author: authorList){
            OwnerShip ownership = calcOwnerShipForAuthor(listOfFileOwnerShips, author);
            ownerShips.add(ownership);
        }

        return ownerShips;

    }

    private OwnerShip calcOwnerShipForAuthor(List<OwnerShipOneFile> listOfOwnerShips, Author author) {
        OwnerShip resultOwnerShip = new OwnerShip(author);

        for(OwnerShipOneFile ownerShipOneFile : listOfOwnerShips){
            OwnerShip tmpOwnerShip = ownerShipOneFile.getOwnerShipOfAuthor(author);
            resultOwnerShip.addOwnerShip(tmpOwnerShip);
        }

        return resultOwnerShip;
    }

    /**
     * calc ownership for specific author with the given list of commits and the list of fileChanges
     * @param author specific author
     * @param commitList list of commits
     * @param fileChangeList list of fileChanges for the commits
     * @param nrOfChangedLinesInFile sum of all insertions and deletions for the file
     * @return ownership for the author
     */
    private OwnerShip getOwnerShip(Author author, List<Commit> commitList, List<FileChange> fileChangeList, int nrOfChangedLinesInFile) {
        OwnerShip ownerShip = new OwnerShip();
        ownerShip.setNrOfChangedLinesInFile(nrOfChangedLinesInFile);
        ownerShip.setName(author.getName());

        //gehe jeden fileChange durch
        for(FileChange fc : fileChangeList){
            //suche nach passendem commit
            for(Commit c: commitList){
                if(c.getId() == fc.getCommitId()){
                    //beachte nur commits, mit id = author.id
                    if(c.getAuthorId() == author.getId()){
                        ownerShip.addInsertions(fc.getInsertions());
                        ownerShip.addDeletions(fc.getDeletions());
                    }
                    //verlasse schleife, sobald commit gefunden
                    break;
                }
            }
        }

        return ownerShip;
    }

    //berechnet, wie viele zeilen in einem file insgesamt hinzugefuegt bzw. geloescht wurden (insertions + deletions)
    private int calcNrOfLinesChangedInFile(File f, List<FileChange> fileChangeList) {
        int nrOfLinesChanged = 0;

        for (FileChange fc : fileChangeList) {
            nrOfLinesChanged += fc.getInsertions() + fc.getDeletions();
        }

        return nrOfLinesChanged;

    }
}
