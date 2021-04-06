package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ProjectStructure {

    private String name;
    private boolean isFolder;

    //@JsonIgnore
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

    public static ProjectStructure pathToProjectStructure(String path) {

        //path without folders
        if (!path.contains("/")) {
            return new ProjectStructure(
                    path,
                    false,
                    new ArrayList<>()
            );
        }

        ProjectStructure currentProjectStructure = null;
        ProjectStructure lastProjectStructure = null;
        boolean loop = true;
        while (loop) {
            currentProjectStructure = new ProjectStructure();
            String name;

            if (path.contains("/")) {
                name = path.substring(path.lastIndexOf("/") + 1);
                path = path.substring(0, path.indexOf("/"));
            } else {
                name = path;
                loop = false;
            }

            currentProjectStructure.setName(name);

            if (lastProjectStructure != null) {
                currentProjectStructure.getChildren().add(lastProjectStructure);
                currentProjectStructure.setFolder(true);
            } else {
                currentProjectStructure.setName(name);
                currentProjectStructure.setFolder(false);
                currentProjectStructure.setChildren(new ArrayList<>());
            }

            lastProjectStructure = currentProjectStructure;

        }

        return currentProjectStructure;
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
