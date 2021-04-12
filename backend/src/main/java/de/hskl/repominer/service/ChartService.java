package de.hskl.repominer.service;

import de.hskl.repominer.models.chart.ChartContext;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.RequestableChart;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.datagetter.CodeOwnerShipGetter;
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

        RequestableChart ownerShipChart = new RequestableChart("folder", "ownerShip",
                Collections.singleton(new ChartContext(ChartContext.ViewContext.FILE_BROWSER,
                        new HashSet<>(Arrays.asList(ChartContext.SubContext.FOLDER, ChartContext.SubContext.FILE)))),
                new CodeOwnerShipGetter());
        requestableCharts.put(ownerShipChart.getName(), ownerShipChart);
    }

    public Set<RequestableChart> getByContext(ChartContext.ViewContext viewContext) {
        return this.requestableCharts.values()
                .stream()
                .filter(x -> x.getAvailableContext()
                        .stream()
                        .anyMatch(ctx -> ctx.getViewContext() == viewContext))
                .collect(Collectors.toSet());
    }


    public AbstractChart<?> getChart(int projectId, String chartName, ChartRequestMeta crm) {
        return requestableCharts.get(chartName).getData(projectId, crm, projectService);
    }



}
