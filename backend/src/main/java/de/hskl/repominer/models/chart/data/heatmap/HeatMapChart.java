package de.hskl.repominer.models.chart.data.heatmap;

import de.hskl.repominer.models.chart.YXAxisChart;

import java.util.List;

public class HeatMapChart extends YXAxisChart<int[]> {
    private List<String> yAxisCategories;
    private List<String> xAxisCategories;


    @Override
    public String getType() {
        return "heatmap";
    }

    public List<String> getyAxisCategories() {
        return yAxisCategories;
    }

    public void setyAxisCategories(List<String> yAxisCategories) {
        this.yAxisCategories = yAxisCategories;
    }

    public List<String> getxAxisCategories() {
        return xAxisCategories;
    }

    public void setxAxisCategories(List<String> xAxisCategories) {
        this.xAxisCategories = xAxisCategories;
    }
}
