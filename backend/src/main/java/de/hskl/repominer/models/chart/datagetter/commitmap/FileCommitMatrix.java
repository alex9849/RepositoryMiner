package de.hskl.repominer.models.chart.datagetter.commitmap;

import java.util.Arrays;
import java.util.List;

public class FileCommitMatrix {
    private int[][] matrix;
    private String[] xContext;
    private String[] yContext;

    public FileCommitMatrix(int sizeM, int sizeN) {
        this.matrix = new int[sizeM][sizeN];
        this.xContext = new String[sizeN];
        this.yContext = new String[sizeM];
    }
    
    public int getValue(int x, int y) {
        return this.matrix[x][y];
    }
    
    public void setValue(int x, int y, int value) {
        this.matrix[x][y] = value;
    }
    
    public void setXContext(int x, String value) {
        this.xContext[x] = value;
    }
    
    public void setYContext(int y, String value) {
        this.yContext[y] = value;
    }
    
    public String getXContext(int x) {
        return this.xContext[x];
    }

    public List<String> getXContextList() {
        return Arrays.asList(this.xContext);
    }

    public List<String> getYContextList() {
        return Arrays.asList(this.yContext);
    }

    public int getM() {
        return this.yContext.length;
    }

    public int getN() {
        return this.xContext.length;
    }
    
    public String getYContext(int y) {
        return this.yContext[y];
    }
    
    public FileCommitMatrix transpose() {
        FileCommitMatrix transposed = new FileCommitMatrix(this.xContext.length, this.yContext.length);
        for(int i = 0; i < this.matrix.length; i++) {
            transposed.setXContext(i, this.getYContext(i));
            for(int j = 0; j < this.matrix[i].length; j++) {
                transposed.setYContext(j, this.getXContext(j));
                transposed.setValue(j, i, this.matrix[i][j]);
            }
        }
        return transposed;
    }

    public FileCommitMatrix multiply(FileCommitMatrix right) {
        if(this.xContext.length != right.yContext.length) {
            throw new IllegalArgumentException("n of current matrix and m of right matrix are not equal!");
        }

        FileCommitMatrix result = new FileCommitMatrix(this.yContext.length, right.xContext.length);
        for(int i = 0; i < this.matrix.length; i++) {
            result.yContext[i] = this.yContext[i];
            for(int j = 0; j < right.xContext.length; j++) {
                result.xContext[j] = right.xContext[j];
                int value = 0;
                for(int z = 0; z < this.matrix[i].length; z++) {
                    value += this.matrix[i][z] * right.matrix[z][j];
                }
                result.setValue(i, j, value);
            }
        }
        return result;
    }

}
