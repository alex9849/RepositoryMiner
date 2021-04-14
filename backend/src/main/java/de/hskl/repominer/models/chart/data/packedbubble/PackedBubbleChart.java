package de.hskl.repominer.models.chart.data.packedbubble;

import de.hskl.repominer.models.chart.data.AbstractChart;

public class PackedBubbleChart extends AbstractChart<PackedBubbleChartDatum> {

    @Override
    public String getType() {
        return "packedbubble";
    }
}
