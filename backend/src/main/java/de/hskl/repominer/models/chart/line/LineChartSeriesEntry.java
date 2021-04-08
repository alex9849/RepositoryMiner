package de.hskl.repominer.models.chart.line;

import de.hskl.repominer.models.chart.ISeriesEntry;

import java.util.List;

public class LineChartSeriesEntry implements ISeriesEntry {
    private String name;
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
