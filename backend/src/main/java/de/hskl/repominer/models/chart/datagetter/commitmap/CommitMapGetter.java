package de.hskl.repominer.models.chart.datagetter.commitmap;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.heatmap.HeatMapChart;
import de.hskl.repominer.service.ProjectService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommitMapGetter implements ChartDataGetter {
    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        FileCommitMatrix fileCommitMatrix = projectService.getFileCommitMatrix(projectId, crm.path);
        FileCommitMatrix togetherCommittedMatrix = fileCommitMatrix.multiply(fileCommitMatrix.transpose());

        HeatMapChart heatMapChart = new HeatMapChart();
        heatMapChart.setxAxisTitle("Files");
        heatMapChart.setyAxisTitle("Files");
        heatMapChart.setxAxisCategories(togetherCommittedMatrix.getXContextList());
        heatMapChart.setyAxisCategories(togetherCommittedMatrix.getYContextList());
        SeriesEntry<int[]> series = new SeriesEntry<int[]>().setName("Commits");
        heatMapChart.setSeries(Collections.singletonList(series));
        List<int[]> data = new LinkedList<>();
        for(int i = 0; i < togetherCommittedMatrix.getM(); i++) {
            for(int j = 0; j < togetherCommittedMatrix.getN(); j++) {
                data.add(new int[]{i, j, togetherCommittedMatrix.getValue(i, j)});
            }
        }
        series.setData(new ArrayList<>(data));
        heatMapChart.setName("Together committed");
        heatMapChart.setDescription("File pairs and how often they have been committed together");
        return heatMapChart;
    }

}
