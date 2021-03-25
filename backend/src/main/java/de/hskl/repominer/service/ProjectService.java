package de.hskl.repominer.service;

import de.hskl.repominer.models.*;
import de.hskl.repominer.repository.*;
import de.hskl.repominer.service.logparser.LogParser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final CommitRepository commitRepo;
    private final FileChangeRepository fileChangeRepo;
    private final FileRepository fileRepo;
    private final AuthorRepository authorRepo;

    public ProjectService(ProjectRepository projectRepo, CommitRepository commitRepo,
                          FileChangeRepository fileChangeRepo, FileRepository fileRepo,
                          AuthorRepository authorRepo) {
        this.projectRepo = projectRepo;
        this.commitRepo = commitRepo;
        this.fileChangeRepo = fileChangeRepo;
        this.fileRepo = fileRepo;
        this.authorRepo = authorRepo;
    }

    @Transactional
    public Project addProject(BufferedReader logInputStream) throws IOException, ParseException {
        //Todo implement
        InputStream stream = ProjectService.class.getClassLoader().getResourceAsStream("testLog.txt");

        Project project = LogParser.parseLogStream(new BufferedReader(new InputStreamReader(stream)));
        Project savedProject = projectRepo.saveProject(project);
        for (Author author : project.getAuthors()) {
            author.setProjectId(savedProject.getId());
            Author savedAuthor = authorRepo.saveAuthor(author);
            author.setId(savedAuthor.getId());
        }
        for(Commit commit : project.getCommits()) {
            commit.setAuthorId(commit.getAuthor().getId());
            commit.setProjectId(savedProject.getId());
            Commit savedCommit = commitRepo.saveCommit(commit);
            commit.setId(savedCommit.getId());
            for(FileChange fileChange : commit.getFileChanges()) {
                //Id of saved objects is always > 0
                if(fileChange.getFile().getId() <= 0) {
                    fileChange.getFile().setProjectId(savedProject.getId());
                    File savedFile = fileRepo.saveFile(fileChange.getFile());
                    fileChange.getFile().setId(savedFile.getId());
                }
                fileChange.setCommitId(commit.getId());
                fileChange.setFileId(fileChange.getFile().getId());
                fileChangeRepo.saveFileChange(fileChange);
            }
        }

        return savedProject;
    }

}
