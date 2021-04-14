package de.hskl.repominer.models.chart.data.pie;

public class PieChartDatum {
    private String name;
    private double value;

    public String getName() {
        return name;
    }

    public PieChartDatum setName(String name) {
        this.name = name;
        return this;
    }

    public double getValue() {
        return value;
    }

    public PieChartDatum setValue(double value) {
        this.value = value;
        return this;
    }
}
