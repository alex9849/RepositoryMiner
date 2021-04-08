package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.ISeriesEntry;

import java.util.List;

public class PackedBubbleChartSeriesEntry implements ISeriesEntry {
    private String name;
    private List<PackedBubbleChartSeriesEntryDatum> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(List<PackedBubbleChartSeriesEntryDatum> data) {
        this.data = data;
    }

    @Override
    public List<PackedBubbleChartSeriesEntryDatum> getData() {
        return data;
    }
}
