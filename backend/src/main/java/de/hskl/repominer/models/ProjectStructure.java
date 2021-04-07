package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectStructure {

    private String name;
    private boolean isFolder;
    private List<ProjectStructure> children;

    public ProjectStructure() {
        this.children = new ArrayList<>();
    }

    ;

    public ProjectStructure(String name, boolean isFolder, List<ProjectStructure> children) {
        this.name = name;
        this.isFolder = isFolder;
        this.children = children;
    }

    /*  - transforms the given path in the scanner into a projectStructure an returns it.
        - returns null and appends the projectstrucutre to the projectStructureList if a directory in the path already exists
          in the projectStructureList
     */
    public static ProjectStructure pathToProjectStructure(Scanner scanner, List<ProjectStructure> projectStructureList, boolean duplicate) {
        String dirName = null;
        boolean isFile = false;
        boolean loop = true;
        boolean isDuplicate = false;
        ProjectStructure resultProjectStructure = null;
        ProjectStructure currentProjectStructure = null;
        ProjectStructure lastProjectStructure = null;
        ProjectStructure file = null;

        scanner.useDelimiter("/");

        //go through every path component
        while (scanner.hasNext()) {
            dirName = scanner.next();

            //hasNext == false => is file
            if (!scanner.hasNext()) {
                file = new ProjectStructure(
                        dirName,
                        false,
                        new ArrayList<>()
                );
            } else {
                currentProjectStructure = new ProjectStructure(
                        dirName,
                        true,
                        new ArrayList<>()
                );
            }//folder

            if (loop) {
                //check if folder already exists
                for (ProjectStructure ps : projectStructureList) {
                    String psName = ps.getName();

                    //if folder exists
                    if (psName.equals(dirName)) {
                        isDuplicate = true;
                        return pathToProjectStructure(scanner, ps.getChildren(), isDuplicate);
                    }
                }
                //no folder with the same name exists -> no duplicates -> no more iterations
                if (!isDuplicate) {
                    loop = false;
                }
            }

            //recursion (duplicate) and no duplicates found (!isDuplicate) => create folder strucutre or file and append to list
            if (duplicate && !isDuplicate) {
                //folder exists and contains file
                if (resultProjectStructure != null && file != null) {
                    lastProjectStructure.getChildren().add(file);
                    projectStructureList.add(resultProjectStructure);
                    return null;
                }
                //no folders only file
                if (file != null) {
                    projectStructureList.add(file);
                    return null;
                }
            }

            //init result projectStructure
            if (resultProjectStructure == null)
                resultProjectStructure = currentProjectStructure;

            //"file.txt"
            if (lastProjectStructure == null && file != null)
                return file;
            else {
                if (file != null) {
                    lastProjectStructure.getChildren().add(file);
                    return resultProjectStructure;
                }//end of path reached

                if (lastProjectStructure != null) {
                    lastProjectStructure.getChildren().add(currentProjectStructure);
                }//insert folder into childrenList
            }

            lastProjectStructure = currentProjectStructure;
        }
        return resultProjectStructure;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public List<ProjectStructure> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectStructure> children) {
        this.children = children;
    }
}
