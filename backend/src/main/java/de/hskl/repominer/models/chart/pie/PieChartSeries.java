package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.ISeries;

import java.util.List;

public class PieChartSeries implements ISeries {
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
