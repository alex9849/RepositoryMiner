package de.hskl.repominer.models.chart.data.packedbubble;

import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.pie.PieChartDatum;

public class PackedBubbleChartDatum extends PieChartDatum {
    private SeriesEntry<PackedBubbleChartDatum> drilldown;

    public SeriesEntry<PackedBubbleChartDatum> getDrilldown() {
        return drilldown;
    }

    public void setDrilldown(SeriesEntry<PackedBubbleChartDatum> drilldown) {
        this.drilldown = drilldown;
    }
}
