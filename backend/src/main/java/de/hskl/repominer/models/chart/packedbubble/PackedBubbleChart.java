package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.AbstractChart;

public class PackedBubbleChart extends AbstractChart<PackedBubbleChartSeriesEntryDatum> {

    @Override
    public String getType() {
        return "packedbubble";
    }
}
