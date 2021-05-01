package de.hskl.repominer.models.chart.datagetter.commitmap;

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

    public int getM() {
        return this.yContext.length;
    }

    public int getN() {
        return this.xContext.length;
    }

    public String[] getXContext() {
        return xContext;
    }

    public void setXContext(String[] xContext) {
        this.xContext = xContext;
    }

    public String[] getYContext() {
        return yContext;
    }

    public void setYContext(String[] yContext) {
        this.yContext = yContext;
    }
    
    public FileCommitMatrix transpose() {
        FileCommitMatrix transposed = new FileCommitMatrix(this.xContext.length, this.yContext.length);
        for(int i = 0; i < this.matrix.length; i++) {
            transposed.xContext[i] = this.yContext[i];
            for(int j = 0; j < this.matrix[i].length; j++) {
                transposed.yContext[j] = this.xContext[j];
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
