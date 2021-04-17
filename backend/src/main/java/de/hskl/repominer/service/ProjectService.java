package de.hskl.repominer.service;

import de.hskl.repominer.models.*;
import de.hskl.repominer.models.chart.datagetter.OwnerShip;
import de.hskl.repominer.repository.*;
import de.hskl.repominer.service.logparser.LogParser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepo;
    private final CommitRepository commitRepo;
    private final FileChangeRepository fileChangeRepo;
    private final FileRepository fileRepo;
    private final AuthorRepository authorRepo;
    private final CurrentPathRepository currPathRepo;
    private final LogAuthorRepository logAuthorRepo;


    public ProjectService(ProjectRepository projectRepo, CommitRepository commitRepo,
                          FileChangeRepository fileChangeRepo, FileRepository fileRepo,
                          AuthorRepository authorRepo, CurrentPathRepository currPathRepo,
                          LogAuthorRepository logAuthorRepo) {
        this.projectRepo = projectRepo;
        this.commitRepo = commitRepo;
        this.fileChangeRepo = fileChangeRepo;
        this.fileRepo = fileRepo;
        this.authorRepo = authorRepo;
        this.currPathRepo = currPathRepo;
        this.logAuthorRepo = logAuthorRepo;
    }

    public Project addProject(String projectName, BufferedReader logInputStream) throws IOException, ParseException {
        Project project = LogParser.parseLogStream(logInputStream);
        project.setName(projectName);
        Project savedProject = projectRepo.addProject(project);
        for (Author author : project.getAuthors()) {
            author.setProjectId(savedProject.getId());
            author = authorRepo.addAuthor(author);
            for(LogAuthor logAuthor : author.getLogAuthors()) {
                logAuthor.setAuthorId(author.getId());
                logAuthor.setProjectId(project.getId());
                logAuthorRepo.addLogAuthor(logAuthor);
            }
        }
        for(Commit commit : project.getCommits()) {
            commit.setAuthorId(commit.getAuthor().getId());
            commit.setProjectId(savedProject.getId());
            Commit savedCommit = commitRepo.addCommit(commit);
            commit.setId(savedCommit.getId());
            for(FileChange fileChange : commit.getFileChanges()) {
                //Id of saved objects is always > 0
                if(fileChange.getFile().getId() <= 0) {
                    fileChange.getFile().setProjectId(savedProject.getId());
                    File savedFile = fileRepo.addFile(fileChange.getFile());
                    fileChange.getFile().setId(savedFile.getId());
                }
                fileChange.setCommitId(commit.getId());
                fileChange.setFileId(fileChange.getFile().getId());
                fileChangeRepo.addFileChange(fileChange);
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

    public List<CurrentPath> getAllCurrentPaths(int projectId) { return currPathRepo.getAllCurrentPaths(projectId);}

    public List<ProjectStructure> generateFileTree(int projectId) {
        List<CurrentPath> pathList = this.getAllCurrentPaths(projectId);
        List<ProjectStructure> projStructureList = new ArrayList<>();

        //parse paths to projectstructure
        for(CurrentPath cp : pathList){
            if(cp.getPath() == null) {
                continue;
            }
            Scanner sc = new Scanner(cp.getPath());
            ProjectStructure ps = ProjectStructure.pathToProjectStructure(sc, projStructureList,false);
            if(ps != null ) {
                projStructureList.add(ps);
            }

            ProjectStructure.sortProjectStructureListAlphabeticalWithFolderPriority(projStructureList);
        }
        return projStructureList;
    }

    public List<OwnerShip> getOwnerShip(int projectId, String path) {
        return projectRepo.getOwnerShip(projectId, path);
    }

    public List<OwnerShip> getOwnerShipDevelopment(int projectId, String path) {
        return projectRepo.getOwnerShipDevelopment(projectId, path);
    }

    public List<OwnerShip> getOwnerShipForFolder(int projectId, String path) {
        return projectRepo.getOwnerShipForFolder(projectId, path);
    }

    public List<LogAuthor> getLogAuthors(int projectId) {
        return logAuthorRepo.loadLogAuthorsForProject(projectId);
    }

    public List<Author> getAuthors(int projectId) {
        List<Author> authors = authorRepo.loadAuthorsForProject(projectId);
        Map<Integer, List<LogAuthor>> authorIdToLogAuthor = logAuthorRepo.loadLogAuthorsForProject(projectId)
                .stream().filter(x -> x.getAuthorId() != null).collect(Collectors.groupingBy(LogAuthor::getAuthorId));
        for(Author author : authors) {
            if(authorIdToLogAuthor.containsKey(author.getId())) {
                author.setLogAuthors(authorIdToLogAuthor.get(author.getId()));
            } else {
                author.setLogAuthors(new ArrayList<>());
            }
        }
        return authors;
    }
}
