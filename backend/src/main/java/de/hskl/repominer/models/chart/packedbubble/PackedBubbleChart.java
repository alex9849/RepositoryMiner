package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.AbstractChart;

public class PackedBubbleChart extends AbstractChart<PackedBubbleChartDatum> {

    @Override
    public String getType() {
        return "packedbubble";
    }
}
