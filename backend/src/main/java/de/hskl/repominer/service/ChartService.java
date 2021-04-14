package de.hskl.repominer.service;

import de.hskl.repominer.models.chart.ChartContext;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.RequestableChart;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.datagetter.CodeOwnerShipFolderGetter;
import de.hskl.repominer.models.chart.datagetter.CodeOwnerShipGetter;
import de.hskl.repominer.models.chart.datagetter.ComeOwnershipDevelopmentGetter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private final ProjectService projectService;
    private final Map<String, RequestableChart> requestableCharts;

    public ChartService(ProjectService projectService) {
        this.requestableCharts = new HashMap<>();
        this.projectService = projectService;

        RequestableChart ownerShipFileChart = new RequestableChart(
                "groups", "ownershipFile", "Code ownership",
                Collections.singleton(new ChartContext(ChartContext.ViewContext.FILE_BROWSER,
                        new HashSet<>(Arrays.asList(ChartContext.SubContext.FILE)))),
                new CodeOwnerShipGetter());
        requestableCharts.put(ownerShipFileChart.getIdentifier(), ownerShipFileChart);

        RequestableChart ownerShipFolderChart = new RequestableChart("groups", "ownershipFolder",
                "Code ownership",
                Collections.singleton(new ChartContext(ChartContext.ViewContext.FILE_BROWSER,
                        new HashSet<>(Arrays.asList(ChartContext.SubContext.FOLDER)))),
                new CodeOwnerShipFolderGetter());
        requestableCharts.put(ownerShipFolderChart.getIdentifier(), ownerShipFolderChart);

        RequestableChart ownerShipDevelopmentChart = new RequestableChart("groups", "ownershipDevelopment",
                "Code ownership development",
                Collections.singleton(new ChartContext(ChartContext.ViewContext.FILE_BROWSER,
                        new HashSet<>(Arrays.asList(ChartContext.SubContext.FOLDER, ChartContext.SubContext.FILE)))),
                new ComeOwnershipDevelopmentGetter());
        requestableCharts.put(ownerShipDevelopmentChart.getIdentifier(), ownerShipDevelopmentChart);
    }

    public Set<RequestableChart> getByContext(ChartContext.ViewContext viewContext) {
        if(viewContext == null) {
            return new HashSet<>(this.requestableCharts.values());
        }
        return this.requestableCharts.values()
                .stream()
                .filter(x -> x.getAvailableContext()
                        .stream()
                        .anyMatch(ctx -> ctx.getViewContext() == viewContext))
                .collect(Collectors.toSet());
    }


    public AbstractChart<?> getChart(int projectId, String chartIdentifier, ChartRequestMeta crm) {
        RequestableChart rc = requestableCharts.get(chartIdentifier);
        if(rc == null) {
            //Exception
            return null;
        }

        return rc.getData(projectId, crm, projectService);
    }



}
