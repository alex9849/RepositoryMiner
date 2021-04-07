package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.IChart;

public class PieChart implements IChart {
    private PieChartSeries series;
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

    public void setSeries(PieChartSeries series) {
        this.series = series;
    }

    @Override
    public PieChartSeries getSeries() {
        return series;
    }
}
