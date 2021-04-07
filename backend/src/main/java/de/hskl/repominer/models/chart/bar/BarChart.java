package de.hskl.repominer.models.chart.bar;

import de.hskl.repominer.models.chart.IChart;
import de.hskl.repominer.models.chart.ISeries;

public class BarChart implements IChart {
    @Override
    public String getType() {
        return "bar";
    }

    @Override
    public ISeries getSeries() {
        //Todo implement
        return null;
    }
}
