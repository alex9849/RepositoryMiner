package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.line.LineChart;
import de.hskl.repominer.service.ProjectService;

import java.text.SimpleDateFormat;
import java.util.*;

public class ComeOwnershipDevelopmentGetter implements ChartDataGetter {
    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        List<OwnerShip> ownerShips = projectService.getOwnerShipDevelopment(projectId, crm.path);
        Set<String> seenDates = new HashSet<>();

        LineChart lineChart = new LineChart();
        lineChart.setName("Code-Ownership-Development");
        lineChart.setDescription("Changed lines of code by author at the current path /" + crm.path);
        lineChart.setCategories(new ArrayList<>());
        lineChart.setxAxisTitle("Time");
        lineChart.setyAxisTitle("Changed lines");
        Map<String, SeriesEntry<Integer>> authors = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");

        for(OwnerShip os : ownerShips) {
            SeriesEntry<Integer> series = authors.computeIfAbsent(os.getAuthorName(), name -> {
                SeriesEntry<Integer> seriesEntry = new SeriesEntry<>();
                seriesEntry.setData(new ArrayList<>());
                seriesEntry.setName(name);
                return seriesEntry;
            });
            series.getData().add(os.getChangedCode());
            String stringDate = sdf.format(os.getDate());
            if(seenDates.add(stringDate)) {
                lineChart.getCategories().add(stringDate);
            }
        }

        lineChart.setSeries(new ArrayList<>(authors.values()));
        return lineChart;
    }
}
