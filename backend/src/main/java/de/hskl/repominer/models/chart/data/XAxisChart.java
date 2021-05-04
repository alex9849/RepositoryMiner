package de.hskl.repominer.models.chart.data;

public abstract class XAxisChart<SeriesDataType> extends AbstractChart<SeriesDataType> {
    private String xAxisTitle;

    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public void setxAxisTitle(String yAxisTitle) {
        this.xAxisTitle = yAxisTitle;
    }
}
