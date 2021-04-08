package de.hskl.repominer.models.chart.line;

import de.hskl.repominer.models.chart.IChart;
import de.hskl.repominer.models.chart.INamedAxisChart;

import java.util.List;

public class LineChart implements IChart, INamedAxisChart {
    private String xAxisTitle;
    private String yAxisTitle;
    private List<LineChartSeriesEntry> series;

    @Override
    public String getType() {
        return "line";
    }

    public void setSeries(List<LineChartSeriesEntry> series) {
        this.series = series;
    }

    @Override
    public List<LineChartSeriesEntry> getSeries() {
        return series;
    }

    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public void setxAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }
}