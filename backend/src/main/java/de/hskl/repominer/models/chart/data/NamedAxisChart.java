package de.hskl.repominer.models.chart.data;

public abstract class NamedAxisChart<SeriesDataType> extends AbstractChart<SeriesDataType> {
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
}
