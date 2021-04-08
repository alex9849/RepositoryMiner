package de.hskl.repominer.models.chart;

import java.util.List;

public abstract class Chart<SeriesDataType> {
    private String name;
    private String description;
    private List<SeriesEntry<SeriesDataType>> series;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SeriesEntry<SeriesDataType>> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesEntry<SeriesDataType>> series) {
        this.series = series;
    }

    public abstract String getType();

}
