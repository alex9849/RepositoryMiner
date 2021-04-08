package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.Chart;
import de.hskl.repominer.models.chart.NameValueSeriesEntry;

public class PieChart extends Chart<NameValueSeriesEntry> {

    @Override
    public String getType() {
        return "pie";
    }
}
