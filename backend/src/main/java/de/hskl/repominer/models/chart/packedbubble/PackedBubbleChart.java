package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.Chart;

public class PackedBubbleChart extends Chart<PackedBubbleChartSeriesEntryDatum> {

    @Override
    public String getType() {
        return "packedbubble";
    }
}
