package de.hskl.repominer.service;

import de.hskl.repominer.models.*;
import de.hskl.repominer.repository.*;
import de.hskl.repominer.service.logparser.LogParser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final CommitRepository commitRepo;
    private final FileChangeRepository fileChangeRepo;
    private final FileRepository fileRepo;
    private final AuthorRepository authorRepo;
    private final CurrentPathRepository currPathRepo;


    public ProjectService(ProjectRepository projectRepo, CommitRepository commitRepo,
                          FileChangeRepository fileChangeRepo, FileRepository fileRepo,
                          AuthorRepository authorRepo, CurrentPathRepository currPathRepo) {
        this.projectRepo = projectRepo;
        this.commitRepo = commitRepo;
        this.fileChangeRepo = fileChangeRepo;
        this.fileRepo = fileRepo;
        this.authorRepo = authorRepo;
        this.currPathRepo = currPathRepo;
    }

    public Project addProject(String projectName, BufferedReader logInputStream) throws IOException, ParseException {
        Project project = LogParser.parseLogStream(logInputStream);
        project.setName(projectName);
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

    public List<Project> getProjects() {
        return projectRepo.loadProjects();
    }

    public boolean deleteProject(int projectId) {
        return projectRepo.deleteProject(projectId);
    }

    public List<CurrentPath> getAllCurrentPaths() { return currPathRepo.getAllCurrentPaths();}
}
