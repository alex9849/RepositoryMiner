package de.hskl.repominer.models.chart.data;

import java.util.List;

public class SeriesEntry<T> {
    private String name;
    private List<T> data;

    public String getName() {
        return name;
    }

    public SeriesEntry<T> setName(String name) {
        this.name = name;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public SeriesEntry<T> setData(List<T> data) {
        this.data = data;
        return this;
    }
}
