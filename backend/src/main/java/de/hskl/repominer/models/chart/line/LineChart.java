package de.hskl.repominer.models.chart.line;

import de.hskl.repominer.models.chart.NamedAxisChart;

public class LineChart extends NamedAxisChart<Integer> {

    @Override
    public String getType() {
        return "line";
    }

}
