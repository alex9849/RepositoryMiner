package de.hskl.repominer.endpoints;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController()
@RequestMapping( "/api/project")
public class ProjectEndpoint {

    private final ProjectService projectService;

    public ProjectEndpoint(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addProject(@RequestPart(value = "logfile") MultipartFile uploadFile,
                                        UriComponentsBuilder uriBuilder) throws IOException {
        BufferedReader logStream = new BufferedReader(new InputStreamReader(uploadFile.getInputStream()));
        Project project = projectService.addProject(logStream);

        UriComponents uriComponents = uriBuilder.path("/api/project/{id}").buildAndExpand(project.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(project);
    }

}