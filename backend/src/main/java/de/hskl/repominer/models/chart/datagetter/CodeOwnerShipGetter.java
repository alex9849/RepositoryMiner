package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.service.ProjectService;

public class CodeOwnerShipGetter implements ChartDataGetter {

    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        return null;
    }
}
