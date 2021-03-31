package de.hskl.repominer.service.logparser;

import java.util.List;
import java.util.Set;

public class FileModificationHolder {
    List<ParsedFileChange> fileChanges;
    Set<String> createdFiles;
    Set<String> deletedFiles;
}
