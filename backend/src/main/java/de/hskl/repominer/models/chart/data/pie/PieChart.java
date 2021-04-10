package de.hskl.repominer.models.chart.data.pie;

import de.hskl.repominer.models.chart.data.AbstractChart;

public class PieChart extends AbstractChart<PieChartDatum> {

    @Override
    public String getType() {
        return "pie";
    }
}
