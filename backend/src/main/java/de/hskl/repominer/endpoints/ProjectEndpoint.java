package de.hskl.repominer.endpoints;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.chart.ChartContext;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ChartService;
import de.hskl.repominer.service.ProjectService;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;

@RestController()
@RequestMapping( "/api/project")
public class ProjectEndpoint {

    private final ProjectService projectService;
    private final ChartService chartService;

    public ProjectEndpoint(ProjectService projectService, ChartService chartService) {
        this.projectService = projectService;
        this.chartService = chartService;
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

    @RequestMapping(value = "{id}/author", method = RequestMethod.PUT)
    public ResponseEntity<?> saveAuthorsAndLogAuthorsSettings(@PathVariable(value="id") int id,
                                                              @Valid @RequestBody List<Author> authorsList){
        //update projectId's for new created authors in authorsList
        for(Author a : authorsList)
            if(a.getProjectId() == 0) a.setProjectId(id);

        projectService.updateAuthorsAndLogAuthorsSettings(id, authorsList);

        return ResponseEntity.ok().build();
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
        return ResponseEntity.ok(projectService.generateFileTree(projectId));
    }

    @RequestMapping(value = "{id}/chart", method = RequestMethod.GET)
    public ResponseEntity<?> getCharts(@PathVariable("id") int projectId,
                                       @RequestParam("context") ChartContext.ViewContext vContext) {
        return ResponseEntity.ok(chartService.getByContext(vContext));
    }

    @RequestMapping(value = "{id}/chart/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<?> getChart(@PathVariable("id") int projectId,
                                       @PathVariable("identifier") String chartIdentifier,
                                       @RequestParam(value = "path", required = false) String path) throws NotFoundException {
        ChartRequestMeta crm = new ChartRequestMeta();
        crm.path = path;
        AbstractChart<?> chartData = chartService.getChart(projectId, chartIdentifier, crm);
        if(chartData == null) {
            throw new NotFoundException("Chart couldn't be found!");
        }
        return ResponseEntity.ok(chartData);
    }

    @RequestMapping(value = "{id}/logauthor", method = RequestMethod.GET)
    public ResponseEntity<?> getLogAuthors(@PathVariable("id") int projectId) {
        return ResponseEntity.ok(projectService.getLogAuthors(projectId));
    }

    @RequestMapping(value = "{id}/author", method = RequestMethod.GET)
    public ResponseEntity<?> getAutors(@PathVariable("id") int projectId) {
        return ResponseEntity.ok(projectService.getAuthors(projectId));
    }

}
