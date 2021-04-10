package de.hskl.repominer.models.chart.data.line;

import de.hskl.repominer.models.chart.data.AbstractChart;

public class LineChart extends AbstractChart<Integer> {
    private String xAxisTitle;
    private String yAxisTitle;

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

    @Override
    public String getType() {
        return "line";
    }

}
