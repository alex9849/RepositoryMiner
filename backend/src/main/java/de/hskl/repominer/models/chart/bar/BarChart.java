package de.hskl.repominer.models.chart.bar;

import de.hskl.repominer.models.chart.NameValueSeriesEntry;
import de.hskl.repominer.models.chart.NamedAxisChart;

public class BarChart extends NamedAxisChart<NameValueSeriesEntry> {
    @Override
    public String getType() {
        return "bar";
    }

}
