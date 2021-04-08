package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.IChart;

import java.util.Collections;
import java.util.List;

public class PieChart implements IChart {
    private PieChartSeriesEntry series;
    private String name;
    private String description;

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
        return "pie";
    }

    public void setSeries(PieChartSeriesEntry series) {
        this.series = series;
    }

    @Override
    public List<PieChartSeriesEntry> getSeries() {
        return Collections.singletonList(series);
    }
}
