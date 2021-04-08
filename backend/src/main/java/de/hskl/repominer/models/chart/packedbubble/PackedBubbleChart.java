package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.IChart;

import java.util.List;

public class PackedBubbleChart implements IChart {
    private List<PackedBubbleChartSeriesEntry> series;
    private String name;
    private String description;

    @Override
    public String getType() {
        return "packedbubble";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setSeries(List<PackedBubbleChartSeriesEntry> series) {
        this.series = series;
    }

    @Override
    public List<PackedBubbleChartSeriesEntry> getSeries() {
        return series;
    }
}
