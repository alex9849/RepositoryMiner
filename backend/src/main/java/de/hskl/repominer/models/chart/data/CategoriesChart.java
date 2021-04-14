package de.hskl.repominer.models.chart.data;

import java.util.List;

public abstract class CategoriesChart<SeriesDataType> extends XAxisChart<SeriesDataType> {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
