package de.hskl.repominer.service;

import de.hskl.repominer.models.chart.ChartContext;
import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.RequestableChart;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.pie.PieChart;
import de.hskl.repominer.models.chart.data.pie.PieChartDatum;
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
        RequestableChart testChart = new RequestableChart("folder", "testChart", "Test",
                Collections.singleton(new ChartContext(ChartContext.ViewContext.FILE_BROWSER,
                        Collections.singleton(ChartContext.SubContext.FOLDER))), new ChartDataGetter() {
            @Override
            public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
                PieChart pieChart = new PieChart();
                SeriesEntry<PieChartDatum> series = new SeriesEntry<>();
                series.setName("TestSeries");
                List<PieChartDatum> data = new ArrayList<>();
                data.add(new PieChartDatum().setName("Datum 1").setValue(20));
                data.add(new PieChartDatum().setName("Datum 2").setValue(30));
                data.add(new PieChartDatum().setName("Datum 3").setValue(50));
                series.setData(data);
                pieChart.setName("Test");
                pieChart.setDescription("Test description");
                pieChart.setSeries(Collections.singletonList(series));
                return pieChart;
            }
        });
        this.requestableCharts.put(testChart.getIdentifier(), testChart);
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
