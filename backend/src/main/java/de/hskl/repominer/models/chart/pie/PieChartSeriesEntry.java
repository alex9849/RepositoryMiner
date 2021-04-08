package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.ISeriesEntry;

import java.util.List;

public class PieChartSeriesEntry implements ISeriesEntry {
    private String name;
    private List<PieChartDatum> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isColorByPoint() {
        return true;
    }

    public List<PieChartDatum> getData() {
        return data;
    }

    public void setData(List<PieChartDatum> data) {
        this.data = data;
    }
}
