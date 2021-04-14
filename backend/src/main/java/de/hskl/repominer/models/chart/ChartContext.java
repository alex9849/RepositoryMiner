package de.hskl.repominer.models.chart;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ChartContext {
    private final ViewContext viewContext;
    private final Set<SubContext> subContext;


    public ChartContext(ViewContext viewContext, Set<SubContext> subContexts) {
        if(!viewContext.getSubContexts().containsAll(subContexts)) {
            throw new IllegalArgumentException("SubContext not available!");
        }
        this.viewContext = viewContext;
        this.subContext = subContexts;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }

    public Set<SubContext> getSubContext() {
        return subContext;
    }

    public enum SubContext {
        FILE, FOLDER
    }

    public enum ViewContext {
        FILE_BROWSER(SubContext.FILE, SubContext.FOLDER), AUTHORS, GLOBAL;

        private final Set<SubContext> subContexts;

        ViewContext(SubContext... subContexts) {
            if(subContexts != null) {
                this.subContexts = Arrays.stream(subContexts).collect(Collectors.toSet());
            } else {
                this.subContexts = Collections.emptySet();
            }
        }

        public Set<SubContext> getSubContexts() {
            return subContexts;
        }
    }
}
