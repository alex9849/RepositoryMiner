package de.hskl.repominer.service.logparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileModificationHolder {
    List<ParsedFileChange> fileChanges = new ArrayList<>();
    Set<String> createdFiles = new HashSet<>();
    Set<String> deletedFiles = new HashSet<>();

    public boolean isEmpty() {
        return fileChanges.isEmpty()
                && createdFiles.isEmpty()
                && deletedFiles.isEmpty();
    }
}
