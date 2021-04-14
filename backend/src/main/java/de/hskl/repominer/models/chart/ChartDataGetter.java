package de.hskl.repominer.models.chart;

import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ProjectService;

public interface ChartDataGetter {

    AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService);

}
