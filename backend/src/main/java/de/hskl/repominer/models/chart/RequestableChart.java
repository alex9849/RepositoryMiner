package de.hskl.repominer.models.chart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ProjectService;

import java.util.Set;

public class RequestableChart {
    private final String icon;
    private final String identifier;
    private final String name;
    private final Set<ChartContext> availableContext;
    private final ChartDataGetter chartDataGetter;


    public RequestableChart(String icon, String identifier, String name, Set<ChartContext> availableContext, ChartDataGetter dataGetter) {
        this.icon = icon;
        this.identifier = identifier;
        this.name = name;
        this.availableContext = availableContext;
        this.chartDataGetter = dataGetter;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public Set<ChartContext> getAvailableContext() {
        return availableContext;
    }

    @JsonIgnore
    public AbstractChart<?> getData(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        return chartDataGetter.get(projectId, crm, projectService);
    }
}
