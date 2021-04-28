package de.hskl.repominer.models.chart;

import de.hskl.repominer.models.chart.data.XAxisChart;

public abstract class YXAxisChart<SeriesType> extends XAxisChart<SeriesType> {
    private String yAxisTitle;

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }
}
