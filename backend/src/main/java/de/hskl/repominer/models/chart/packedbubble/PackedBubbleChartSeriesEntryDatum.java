package de.hskl.repominer.models.chart.packedbubble;

public class PackedBubbleChartSeriesEntryDatum {
    private String name;
    private int value;
    private PackedBubbleChartSeriesEntry drilldown;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public PackedBubbleChartSeriesEntry getDrilldown() {
        return drilldown;
    }

    public void setDrilldown(PackedBubbleChartSeriesEntry drilldown) {
        this.drilldown = drilldown;
    }
}
