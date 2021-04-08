package de.hskl.repominer.models.chart.bar;

import de.hskl.repominer.models.chart.IChart;
import de.hskl.repominer.models.chart.INamedAxisChart;

import java.util.List;

public class BarChart implements IChart, INamedAxisChart {
    private String xAxisTitle;
    private String yAxisTitle;
    private String name;
    private String description;
    private List<BarChartSeriesEntry> series;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
