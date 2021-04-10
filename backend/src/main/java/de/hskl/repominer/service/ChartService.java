package de.hskl.repominer.service;

import de.hskl.repominer.models.chart.ChartContext;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.RequestableChart;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.datagetter.CodeOwnerShipGetter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private final ProjectService projectService;
    private final Map<String, RequestableChart> requestableCharts;

    public ChartService(ProjectService projectService) {
        RequestableChart ownerShipChart = new RequestableChart("", "ownerShip", null, new CodeOwnerShipGetter());
        this.requestableCharts = new HashMap<>();
        requestableCharts.put(ownerShipChart.getName(), ownerShipChart);
        this.projectService = projectService;
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
