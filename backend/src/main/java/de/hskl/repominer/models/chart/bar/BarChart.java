package de.hskl.repominer.models.chart.bar;

import de.hskl.repominer.models.chart.AbstractChart;

import java.util.List;

public class BarChart extends AbstractChart<Integer> {
    private String xAxisTitle;
    private List<String> categories;

    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public void setxAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String getType() {
        return "bar";
    }

}
