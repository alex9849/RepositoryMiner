package de.hskl.repominer.models.chart.packedbubble;

import de.hskl.repominer.models.chart.NameValueSeriesEntry;
import de.hskl.repominer.models.chart.SeriesEntry;

public class PackedBubbleChartSeriesEntryDatum extends NameValueSeriesEntry {
    private SeriesEntry<PackedBubbleChartSeriesEntryDatum> drilldown;

    public SeriesEntry<PackedBubbleChartSeriesEntryDatum> getDrilldown() {
        return drilldown;
    }

    public void setDrilldown(SeriesEntry<PackedBubbleChartSeriesEntryDatum> drilldown) {
        this.drilldown = drilldown;
    }
}
