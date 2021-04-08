package de.hskl.repominer.models.chart.bar;

import de.hskl.repominer.models.chart.IChart;
import de.hskl.repominer.models.chart.INamedAxisChart;

import java.util.List;

public class BarChart implements IChart, INamedAxisChart {
    private String xAxisTitle;
    private String yAxisTitle;
    private List<BarChartSeriesEntry> series;

    @Override
    public String getType() {
        return "bar";
    }

    public void setSeries(List<BarChartSeriesEntry> series) {
        this.series = series;
    }
    @Override
    public List<BarChartSeriesEntry> getSeries() {
        return series;
    }

    @Override
    public String getxAxisTitle() {
        return xAxisTitle;
    }

    @Override
    public String getyAxisTitle() {
        return yAxisTitle;
    }

    @Override
    public void setxAxisTitle(String title) {
        xAxisTitle = title;
    }

    @Override
    public void setyAxisTitle(String title) {
        yAxisTitle = title;
    }
}
