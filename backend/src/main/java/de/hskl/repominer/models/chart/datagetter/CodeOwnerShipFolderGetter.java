package de.hskl.repominer.models.chart.datagetter;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.packedbubble.PackedBubbleChart;
import de.hskl.repominer.models.chart.data.packedbubble.PackedBubbleChartDatum;
import de.hskl.repominer.service.ProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeOwnerShipFolderGetter implements ChartDataGetter {

    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        List<OwnerShip> ownerShips = projectService.getOwnerShipForFolder(projectId, crm.path);
        PackedBubbleChart packedBubbleChart = new PackedBubbleChart();
        packedBubbleChart.setName("Code-Ownership");
        packedBubbleChart.setDescription("Changed lines of code by author at the current path /" + crm.path);
        Map<String, SeriesEntry<PackedBubbleChartDatum>> pathToSeriesEntry = new HashMap<>();

        for(OwnerShip ownerShip : ownerShips) {
            SeriesEntry<PackedBubbleChartDatum> seriesEntry = pathToSeriesEntry.computeIfAbsent(ownerShip.getPath(),
                    path -> new SeriesEntry<PackedBubbleChartDatum>()
                            .setName(path)
                            .setData(new ArrayList<>()));
            PackedBubbleChartDatum datum = new PackedBubbleChartDatum();
            datum.setName(ownerShip.getAuthorName());
            datum.setValue(ownerShip.getChangedCode());
            seriesEntry.getData().add(datum);
        }

        packedBubbleChart.setSeries(new ArrayList<>(pathToSeriesEntry.values()));
        return packedBubbleChart;
    }
}
