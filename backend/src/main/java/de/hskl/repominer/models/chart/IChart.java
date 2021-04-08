package de.hskl.repominer.models.chart;

import java.util.List;

public interface IChart {

    String getType();

    String getName();

    String getDescription();

    List<? extends ISeriesEntry> getSeries();

}
