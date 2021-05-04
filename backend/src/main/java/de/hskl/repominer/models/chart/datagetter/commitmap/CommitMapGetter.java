package de.hskl.repominer.models.chart.datagetter.commitmap;

import de.hskl.repominer.models.chart.ChartDataGetter;
import de.hskl.repominer.models.chart.ChartRequestMeta;
import de.hskl.repominer.models.chart.data.AbstractChart;
import de.hskl.repominer.models.chart.data.SeriesEntry;
import de.hskl.repominer.models.chart.data.heatmap.HeatMapChart;
import de.hskl.repominer.service.ProjectService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CommitMapGetter implements ChartDataGetter {
    private FileCommitMatrix.Sorting sortBy;

    public CommitMapGetter(FileCommitMatrix.Sorting sorting) {
        this.sortBy = sorting;
    }

    @Override
    public AbstractChart<?> get(int projectId, ChartRequestMeta crm, ProjectService projectService) {
        if(crm.path == null) {
            throw new IllegalArgumentException("Meta path required!");
        }
        FileCommitMatrix fileCommitMatrix = projectService.getFileCommitMatrix(projectId, crm.path, sortBy);
        FirstFolderExtractor path1FolderMapper = new FirstFolderExtractor();
        fileCommitMatrix.setYContext(Arrays.stream(fileCommitMatrix.getYContext())
                .map(x -> x.substring(crm.path.length()))
                //.map(path1FolderMapper)
                .toArray(String[]::new));

        /*
        if(crm.path2 != null) {
            FileCommitMatrix fileCommitMatrixPath2 = projectService.getFileCommitMatrix(projectId, crm.path2);
            FirstFolderMapper path2FolderMapper = new FirstFolderMapper(crm.path2);
            fileCommitMatrixPath2.setYContext(Arrays.stream(fileCommitMatrixPath2.getYContext())
                    .map(path2FolderMapper)
                    .toArray(String[]::new));
            fileCommitMatrix = mergeMatrix(fileCommitMatrix, fileCommitMatrixPath2);
        }*/


        FileCommitMatrix togetherCommittedMatrix = fileCommitMatrix.multiply(fileCommitMatrix.transpose());

        HeatMapChart heatMapChart = new HeatMapChart();
        heatMapChart.setxAxisTitle("Files");
        heatMapChart.setyAxisTitle("Files");
        heatMapChart.setxAxisCategories(Arrays.asList(togetherCommittedMatrix.getXContext()));
        heatMapChart.setyAxisCategories(Arrays.asList(togetherCommittedMatrix.getYContext()));
        SeriesEntry<int[]> series = new SeriesEntry<int[]>().setName("Commits");
        heatMapChart.setSeries(Collections.singletonList(series));
        List<int[]> data = new LinkedList<>();
        for(int i = 0; i < togetherCommittedMatrix.getM(); i++) {
            for(int j = 0; j < togetherCommittedMatrix.getN(); j++) {
                data.add(new int[]{i, j, togetherCommittedMatrix.getValue(i, j)});
            }
        }
        series.setData(new ArrayList<>(data));
        heatMapChart.setName("Together committed");
        heatMapChart.setDescription("File pairs and how often they have been committed together");
        return heatMapChart;
    }

    private FileCommitMatrix mergeMatrix(FileCommitMatrix matrix1, FileCommitMatrix matrix2) {
        Set<String> differentCommits = new HashSet<>(Arrays.asList(matrix1.getXContext()));
        differentCommits.addAll(Arrays.asList(matrix2.getXContext()));
        Set<String> differentFiles = new HashSet<>(Arrays.asList(matrix1.getYContext()));
        differentFiles.addAll(Arrays.asList(matrix2.getYContext()));

        Map<String, Integer> commitToIndexMap = new HashMap<>();
        Map<String, Integer> fileToIndexMap = new HashMap<>();

        AtomicInteger currentCommitIndex = new AtomicInteger();
        differentCommits.forEach(c -> commitToIndexMap.put(c, currentCommitIndex.getAndIncrement()));
        AtomicInteger currentFileIndex = new AtomicInteger();
        differentFiles.forEach(c -> fileToIndexMap.put(c, currentFileIndex.getAndIncrement()));

        FileCommitMatrix mergedMatrix = new FileCommitMatrix(differentFiles.size(), differentCommits.size());
        for(Map.Entry<String, Integer> entry : commitToIndexMap.entrySet()) {
            mergedMatrix.setXContext(entry.getValue(), entry.getKey());
        }
        for(Map.Entry<String, Integer> entry : fileToIndexMap.entrySet()) {
            mergedMatrix.setYContext(entry.getValue(), entry.getKey());
        }

        for(int i = 0; i < matrix1.getM(); i++) {
            for(int j = 0; j < matrix1.getN(); j++) {
                int newIndexI = fileToIndexMap.get(matrix1.getYContext()[i]);
                int newIndexJ = commitToIndexMap.get(matrix1.getXContext()[j]);
                mergedMatrix.setValue(newIndexI, newIndexJ, matrix1.getValue(i, j));
            }
        }
        for(int i = 0; i < matrix2.getM(); i++) {
            for(int j = 0; j < matrix2.getN(); j++) {
                int newIndexI = fileToIndexMap.get(matrix2.getYContext()[i]);
                int newIndexJ = commitToIndexMap.get(matrix2.getXContext()[j]);
                mergedMatrix.setValue(newIndexI, newIndexJ, matrix2.getValue(i, j));
            }
        }

        return mergedMatrix;
    }

    private static class FirstFolderExtractor implements Function<String, String> {

        @Override
        public String apply(String x) {
            int endindex;
            boolean startsWithSlash = x.startsWith("/");
            if(x.indexOf("/", startsWithSlash? 1 : 0) == -1) {
                endindex = x.length();
            } else {
                endindex = x.indexOf("/", startsWithSlash? 1 : 0);
            }
            return x.substring(0, endindex);
        }
    }

}
