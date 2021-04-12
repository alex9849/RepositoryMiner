package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.line.LineChart;
import de.hskl.repominer.service.ProjectService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComeOwnershipDevelopmentGetter implements ChartDataGetter {
    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        List<OwnerShip> ownerShips = projectService.getOwnerShipDevelopment(projectId, crm.path);

        LineChart lineChart = new LineChart();
        lineChart.setName("Code-Ownership-Development");
        lineChart.setDescription("Changed lines of code by author at the current path " + crm.path);
        lineChart.setCategories(new ArrayList<>());
        lineChart.setxAxisTitle("Time");
        lineChart.setyAxisTitle("Changed lines");
        Map<String, SeriesEntry<Integer>> authors = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

        for(OwnerShip os : ownerShips) {
            SeriesEntry<Integer> series = authors.computeIfAbsent(os.getAuthorName(), name -> {
                SeriesEntry<Integer> seriesEntry = new SeriesEntry<>();
                seriesEntry.setData(new ArrayList<>());
                seriesEntry.setName(name);
                return seriesEntry;
            });
            series.getData().add(os.getChangedCode());
            if(authors.size() == 1) {
                //Add the Date-Axis (only for the first Author to prevent duplicates
                lineChart.getCategories().add(sdf.format(os.getDate()));
            }
        }

        lineChart.setSeries(new ArrayList<>(authors.values()));
        return lineChart;
    }
}
