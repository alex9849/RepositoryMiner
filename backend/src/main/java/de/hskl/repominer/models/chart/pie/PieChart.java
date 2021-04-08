package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.IChart;

import java.util.Collections;
import java.util.List;

public class PieChart implements IChart {
    private PieChartSeriesEntry series;
    private String unit;

    @Override
    public String getType() {
        return "pie";
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setSeries(PieChartSeriesEntry series) {
        this.series = series;
    }

    @Override
    public List<PieChartSeriesEntry> getSeries() {
        return Collections.singletonList(series);
    }
}
