package de.hskl.repominer.endpoints;

import de.hskl.repominer.models.CurrentPath;
import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.ProjectStructure;
import de.hskl.repominer.service.ProjectService;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping( "/api/project")
public class ProjectEndpoint {

    private final ProjectService projectService;

    public ProjectEndpoint(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addProject(@RequestPart(value = "logfile") MultipartFile uploadFile,
                                        @RequestPart(value = "name") String projectName,
                                        UriComponentsBuilder uriBuilder) throws IOException, ParseException {
        BufferedReader logStream = new BufferedReader(new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_16));
        Project project = projectService.addProject(projectName, logStream);

        UriComponents uriComponents = uriBuilder.path("/api/project/{id}").buildAndExpand(project.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(project);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getProjects() {
        return ResponseEntity.ok(projectService.getProjects());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProject(@PathVariable(value = "id") int projectId) throws NotFoundException {
        if(!projectService.deleteProject(projectId)) {
            throw new NotFoundException("Project not found!");
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "{id}/structure", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectStructure(@PathVariable(value = "id") int projectId) {
        List<CurrentPath> pathList = projectService.getAllCurrentPaths();
        List<ProjectStructure> projStructureList = new ArrayList<>();

        for(CurrentPath cp : pathList){
            ProjectStructure ps = ProjectStructure.pathToProjectStructure(cp.getPath());
            projStructureList.add(ps);
        }


        return ResponseEntity.ok(projStructureList);
    }

}
