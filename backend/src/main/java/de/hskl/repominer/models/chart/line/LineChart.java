package de.hskl.repominer.models.chart.line;

import de.hskl.repominer.models.chart.IChart;
import de.hskl.repominer.models.chart.ISeries;

public class LineChart implements IChart {
    private String xAxisTitle;
    private String yAxisTitle;

    @Override
    public String getType() {
        return "line";
    }

    @Override
    public ISeries getSeries() {
        //Todo Implement
        return null;
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
