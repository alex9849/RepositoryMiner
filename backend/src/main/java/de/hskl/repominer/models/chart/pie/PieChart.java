package de.hskl.repominer.models.chart.pie;

import de.hskl.repominer.models.chart.AbstractChart;

public class PieChart extends AbstractChart<PieChartDatum> {

    @Override
    public String getType() {
        return "pie";
    }
}
