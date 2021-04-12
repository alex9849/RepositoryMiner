package de.hskl.repominer.models.chart.data.bar;

import de.hskl.repominer.models.chart.data.CategoriesChart;

public class BarChart extends CategoriesChart<Integer> {

    @Override
    public String getType() {
        return "bar";
    }

}
