package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.pie.PieChart;
import de.hskl.repominer.models.chart.data.pie.PieChartDatum;
import de.hskl.repominer.service.ProjectService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeOwnerShipGetter implements ChartDataGetter {



    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        List<OwnerShip> ownerShips = projectService.getOwnerShip(projectId, crm.path);

        PieChart pieChart = new PieChart();
        pieChart.setName("Code-Ownership");
        pieChart.setDescription("Changed lines of code by author at the current path /" + crm.path);
        SeriesEntry<PieChartDatum> series = new SeriesEntry<>();
        pieChart.setSeries(Collections.singletonList(series));
        series.setName("Changed lines");
        List<PieChartDatum> data = new ArrayList<>();
        series.setData(data);

        for(OwnerShip ownerShip : ownerShips) {
            PieChartDatum datum = new PieChartDatum();
            datum.setName(ownerShip.getAuthorName());
            datum.setValue(ownerShip.getChangedCode());
            data.add(datum);
        }

        return pieChart;
    }

}
