package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.SeriesEntry;
import de.hskl.repominer.models.chart.pie.PieChartDatum;

public class PackedBubbleChartDatum extends PieChartDatum {
    private SeriesEntry<PackedBubbleChartDatum> drilldown;

    public SeriesEntry<PackedBubbleChartDatum> getDrilldown() {
        return drilldown;
    }

    public void setDrilldown(SeriesEntry<PackedBubbleChartDatum> drilldown) {
        this.drilldown = drilldown;
    }
}
