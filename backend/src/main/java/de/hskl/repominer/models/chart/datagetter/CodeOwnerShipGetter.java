package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.File;
import de.hskl.repominer.models.FileChange;
import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CodeOwnerShipGetter implements ChartDataGetter {

    private class OwnerShip {
        int insertions;
        int deletions;
        int nrOfLinesInFile;

        public OwnerShip(){

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

        public int getNrOfLinesInFile() {
            return nrOfLinesInFile;
        }

        public void setNrOfLinesInFile(int nrOfLinesInFile) {
            this.nrOfLinesInFile = nrOfLinesInFile;
        }
    }

    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        System.out.println(crm.path);
        System.out.println("id: " +projectService.getProjectRepo().loadProject(1).getName());

        //calc for single file
        System.out.println(calcOwnershipForProjectAndPath(projectId, crm.path, projectService));

        return null;
    }

    private String calcOwnershipForProjectAndPath(int projectId, String path, ProjectService projectService) {
        List<List<FileChange>> listOfFileChanges = new ArrayList<>();
        int insertions = 0;
        int deletions = 0;

        //list aller files zum project
        List<File> fileList = projectService.getFileRepo().loadAllFilesFromProject(projectId);

        //liste aller authoren fuer proj
        List<Author> authorList = projectService.getAuthorRepo().loadAllAuthorsForPorject(projectId);

        //hole liste aller fileChanges fuer jedes file
        for(File f: fileList){
            List<FileChange> fileChangeList = projectService.getFileChangeRepo().loadAllFileChangesByFileId(f.getId());
            listOfFileChanges.add(fileChangeList);
        }

        List<FileChange> fcList = projectService.getFileChangeRepo().loadAllFileChangesByFileId(3);

        //--WEITERES VORGEHEN HIER EINFUEGEN!!!!


        return null;
    }
}
