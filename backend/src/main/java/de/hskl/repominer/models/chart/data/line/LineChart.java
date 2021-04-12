package de.hskl.repominer.models.chart.data.line;

import de.hskl.repominer.models.chart.data.CategoriesChart;

public class LineChart extends CategoriesChart<Integer> {
    private String yAxisTitle;

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    @Override
    public String getType() {
        return "line";
    }

}
