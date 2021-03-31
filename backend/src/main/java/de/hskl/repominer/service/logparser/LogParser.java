package de.hskl.repominer.service.logparser;

import de.hskl.repominer.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class LogParser {
    private static final String commitHeaderRegex = "^\\[[0-9a-z]+\\] \\[.+\\] \\(([0-9a-z ]+)?\\) \\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d .+";
    private static final String modifiedFileRegex = "(\\d+|-)\t(\\d+|-)\t.+";
    private static final String deletedFileRegex = "(delete) ((mode \\d\\d\\d\\d\\d\\d)|) .+";
    private static final String createdFileRegex = "(create) ((mode \\d\\d\\d\\d\\d\\d)|) .+";
    private static final String otherValidLinesRegex = "(rename|create|mode change) .+";


    public static Project parseLogStream(BufferedReader logInputStream) throws IOException, ParseException {
        Project project = new Project();
        Map<String, Author> nameToAuthorMap = new HashMap<>();
        List<Commit> commits = new LinkedList<>();
        FileTracker fileTracker = null;


        boolean isNewestCommit = true;
        for(ParsedCommit pc : toParsedCommits(toUnparsedCommits(logInputStream))) {
            if(isNewestCommit) {
                fileTracker = new FileTracker(pc.hash);
                isNewestCommit = false;
            }

            boolean isMerge = pc instanceof ParsedMergeCommit;
            if(!isMerge) {
                //Todo Bug: enn eine datei gelöscht wird wird diese durch getFile später wieder erstellt
                //Idee: Alle Dateien, welche im commit verfügbar sind zurück geben und von der map/Liste drauf zugreifen
                fileTracker.addCommit(pc);
            } else {
                fileTracker.addMerge((ParsedMergeCommit) pc);
            }

            List<FileChange> fileChanges = new LinkedList<>();
            Author author = nameToAuthorMap.computeIfAbsent(pc.author, x -> new Author(0, 0, pc.author));
            Commit currentCommit = new Commit(0, 0, pc.hash, isMerge, 0, pc.date, pc.commitMessage);
            currentCommit.setAuthor(author);

            for(ParsedFileChange pFc : pc.changedFiles.fileChanges) {
                String path = pFc.newPath;
                if(pc.changedFiles.deletedFiles.contains(path)) {
                    path = null;
                }

                if(isMerge && (path != null) && !pFc.isRename()) {
                    continue;
                }

                int addedLines = isMerge? 0:pFc.insertions;
                int deletedLines = isMerge? 0:pFc.deletions;
                FileChange currentFileChange = new FileChange(0, 0, path, addedLines, deletedLines);

                File file = fileTracker.getFile(pc.hash, pFc.oldPath);

                currentFileChange.setFile(file);
                fileChanges.add(currentFileChange);
            }

            currentCommit.setFileChanges(fileChanges);
            commits.add(currentCommit);
        }

        project.setCommits(commits);
        project.setAuthors(new ArrayList<>(nameToAuthorMap.values()));
        return project;
    }

    private static List<UnparsedCommit> toUnparsedCommits(BufferedReader logInputStream) throws IOException {

        List<UnparsedCommit> unparsedCommits = new LinkedList<>();
        UnparsedCommit currentCommit = new UnparsedCommit();
        String currentLine;
        while ((currentLine = logInputStream.readLine()) != null) {
            if(currentLine.isEmpty()) {
                continue;
            }
            else if(currentLine.matches(commitHeaderRegex)) {
                if(currentCommit.header != null) {
                    unparsedCommits.add(currentCommit);
                }
                currentCommit = new UnparsedCommit();
                currentCommit.header = currentLine;
            }
            else if(currentLine.matches(modifiedFileRegex)) {
                currentCommit.modifiedFiles.add(currentLine);
            }
            else if(currentLine.trim().matches(deletedFileRegex)) {
                String trimLine = currentLine.trim();
                //delete mode 100644 %fileName% <-- filename begins at index 19
                currentCommit.deletedFiles.add(trimLine.substring(19));
            }
            else if(currentLine.trim().matches(createdFileRegex)) {
                String trimLine = currentLine.trim();
                //create mode 100644 %fileName% <-- filename begins at index 19
                currentCommit.createdFiles.add(trimLine.substring(19));
            }
            else if(currentLine.trim().matches(otherValidLinesRegex)) {
                continue;
            }
            else {
                throw new IllegalArgumentException("Malformed git-log! Make sure the File-Encoding is set to UTF-16!");
            }
        }

        if(currentCommit.header != null) {
            unparsedCommits.add(currentCommit);
        }

        return unparsedCommits;
    }

    private static List<ParsedCommit> toParsedCommits(List<UnparsedCommit> unparsedCommits) throws ParseException {
        List<ParsedCommit> parsedCommits = new LinkedList<>();
        ParsedCommit currentParsedCommit = null;
        boolean appendNext = false;

        for (UnparsedCommit uc : unparsedCommits) {
            if(!appendNext) {
                currentParsedCommit = ParsedCommit.parse(uc.header);
                parsedCommits.add(currentParsedCommit);
            }
            appendNext = (currentParsedCommit instanceof ParsedMergeCommit) && !appendNext;

            FileModificationHolder fmh = new FileModificationHolder();
            fmh.fileChanges = uc.modifiedFiles.stream().map(ParsedFileChange::new).collect(Collectors.toList());
            fmh.deletedFiles = uc.deletedFiles;
            fmh.createdFiles = uc.createdFiles;

            if(currentParsedCommit instanceof ParsedMergeCommit) {
                if(appendNext) {
                    currentParsedCommit.changedFiles = fmh;
                } else {
                    ((ParsedMergeCommit) currentParsedCommit).changedFilesFromLeftTreeSide = fmh;
                }
            } else {
                currentParsedCommit.changedFiles = fmh;
            }
        }

        return parsedCommits;
    }

}
