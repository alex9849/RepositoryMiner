package de.hskl.repominer.service.logparser;

import de.hskl.repominer.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogParser {
    private static final String commitHeaderRegex = "^\\[[0-9a-z]+\\] [0-9a-z-]+ \\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d .+";
    private static final String modifiedFileRegex = "(\\d+|-)\t(\\d+|-)\t.+";
    private static final String deletedFileRegex = "(delete) ((mode \\d\\d\\d\\d\\d\\d)|) .+";
    private static final String otherValidLinesRegex = "(rename|create) .+";
    private static class UnparsedCommit {
        String header;
        List<String> modifiedFiles = new LinkedList<>();
        Set<String> deletedFiles = new HashSet<>();
    }


    public static Project parseLogStream(BufferedReader logInputStream) throws IOException, ParseException {
        Project project = new Project();
        project.setAuthors(new ArrayList<>());
        project.setCommits(new ArrayList<>());

        List<UnparsedCommit> unparsedCommits = new LinkedList<>();
        UnparsedCommit currentCommit = new UnparsedCommit();
        String currentLine;
        while ((currentLine = logInputStream.readLine()) != null) {
            if(currentLine.isEmpty()) {
                if(currentCommit.header == null) {
                    throw new IllegalArgumentException("Malformed git-log!");
                }
                unparsedCommits.add(currentCommit);
                currentCommit = new UnparsedCommit();
            }
            else if(currentLine.matches(commitHeaderRegex)) {
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
            else if(currentLine.trim().matches(otherValidLinesRegex)) {
                continue;
            }
            else {
                throw new IllegalArgumentException("Malformed git-log!");
            }
        }
        Map<String, Author> nameToAuthors = new HashMap<>();
        Map<String, File> pathToFileMap = new HashMap();
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        for (UnparsedCommit currentUnparsedCommit : unparsedCommits) {
            Scanner headerScanner = new Scanner(currentUnparsedCommit.header);
            String stringHash = headerScanner.findInLine("[0-9a-z]+");
            String stringAuthor = headerScanner.findInLine("[0-9a-z-]+");
            String stringDate = headerScanner.findInLine("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
            String commitMessage = headerScanner.findInLine(".+").trim();
            Author author = nameToAuthors.computeIfAbsent(stringAuthor, x -> new Author(0, 0, stringAuthor));
            Commit commit = new Commit(0, 0, stringHash, 0, new Date(dateParser.parse(stringDate).getTime()), commitMessage);
            commit.setAuthor(author);
            List<FileChange> fileChanges = new ArrayList<>();
            for(String fileChangeText : currentUnparsedCommit.modifiedFiles) {
                Scanner fileModifyScanner = new Scanner(fileChangeText);
                int addedLines = 0;
                if(fileModifyScanner.hasNextInt()) {
                    addedLines = fileModifyScanner.nextInt();
                } else {
                    fileModifyScanner.next();
                }
                int deletedLines = 0;
                if(fileModifyScanner.hasNextInt()) {
                    deletedLines = fileModifyScanner.nextInt();
                } else {
                    fileModifyScanner.next();
                }
                String fileName = fileModifyScanner.findInLine(".+").trim();
                File changedFile;
                String path;
                if(!fileName.matches(".+ =\\> .+")) {
                    changedFile = pathToFileMap.computeIfAbsent(fileName, x -> new File(0, 0));
                    if(currentUnparsedCommit.deletedFiles.contains(fileName)) {
                        //If a file gets deleted, we store null
                        path = null;
                    } else {
                        path = fileName;
                    }
                } else {
                    String[] fileNames = fileName.split(" => ", 2);
                    //old filename
                    changedFile = pathToFileMap.remove(fileNames[0]);
                    pathToFileMap.put(fileNames[1], changedFile);
                    path = fileNames[1];
                }
                FileChange fileChange = new FileChange(0, 0, path, addedLines, deletedLines);
                fileChange.setFile(changedFile);
                fileChanges.add(fileChange);
            }
            commit.setFileChanges(fileChanges);
            project.getCommits().add(commit);
        }
        project.getAuthors().addAll(nameToAuthors.values());
        return project;
    }
}
