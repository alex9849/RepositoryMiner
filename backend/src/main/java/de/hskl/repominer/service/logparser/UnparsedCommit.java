package de.hskl.repominer.service.logparser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UnparsedCommit {
    String header;
    List<String> modifiedFiles = new LinkedList<>();
    Set<String> deletedFiles = new HashSet<>();
    Set<String> createdFiles = new HashSet<>();
}
