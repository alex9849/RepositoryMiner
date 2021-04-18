package de.hskl.repominer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LogAuthor {


    private int projectId;
    private int id;
    private String name;
    private Integer authorId;
    @JsonIgnore
    private Author author;

    public LogAuthor(int projectId, int id, String name, int authorId) {
        this.projectId = projectId;
        this.id = id;
        this.name = name;
        this.authorId = authorId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[  ProjektId: " + getProjectId() +
                ", id: " + getId() +
                ", authorId: " + getAuthorId() +
                ", name: " + getName() + "]";
    }

    public static String logAuthorsListToOneLineString(List<LogAuthor> logAuthorList){
        String result = "[ ";
        if(logAuthorList == null)
            return result + "]";

        ListIterator<LogAuthor> it = logAuthorList.listIterator();

        while(it.hasNext()){
            if(it.hasNext()) result += it.next().toString() + ",";
            else result += it.next().toString() + "]";
        }

        return result;
    }
}
