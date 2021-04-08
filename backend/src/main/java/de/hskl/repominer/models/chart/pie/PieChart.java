package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.AbstractChart;
import de.hskl.repominer.models.chart.NameValueSeriesEntry;

public class PieChart extends AbstractChart<NameValueSeriesEntry> {

    @Override
    public String getType() {
        return "pie";
    }
}
