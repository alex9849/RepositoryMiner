package de.hskl.repominer.endpoints;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;

@RestController()
@RequestMapping( "/api/project")
public class ProjectEndpoint {

    private final ProjectService projectService;

    public ProjectEndpoint(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addProject(/*@RequestPart(value = "logfile") MultipartFile uploadFile,*/
                                        UriComponentsBuilder uriBuilder) throws IOException, ParseException {
        //BufferedReader logStream = new BufferedReader(new InputStreamReader(uploadFile.getInputStream()));
        Project project = projectService.addProject(null);

        UriComponents uriComponents = uriBuilder.path("/api/project/{id}").buildAndExpand(project.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(project);
    }

}
