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
        Map<String, ParsedCommit> hashToCommitMap = new HashMap<>();
        Map<String, Author> nameToAuthorMap = new HashMap<>();
        List<Commit> commits = new LinkedList<>();
        Set<String> masterBranchHashes = new HashSet<>();
        FileTracker fileTracker = null;


        boolean isNewestCommit = true;
        for(ParsedCommit pc : toParsedCommits(toUnparsedCommits(logInputStream))) {
            if(isNewestCommit) {
                fileTracker = new FileTracker(pc.hash);
                masterBranchHashes.add(pc.hash);
                isNewestCommit = false;
            }
            if(pc.isMerge) {
                fileTracker.addMerge(pc.hash, pc.parentHash, pc.mergeSourceHash);
            } else {
                fileTracker.addCommit(pc.hash, pc.parentHash);
            }
            //The newest commit is always master! Parent commits of master are always master! Recursive!
            if(masterBranchHashes.contains(pc.hash)) {
                masterBranchHashes.add(pc.parentHash);
            }
            List<FileChange> fileChanges = new LinkedList<>();
            Author author = nameToAuthorMap.computeIfAbsent(pc.author, x -> new Author(0, 0, pc.author));
            Commit currentCommit = new Commit(0, 0, pc.hash, masterBranchHashes.contains(pc.hash), 0, pc.date, pc.commitMessage);
            currentCommit.setAuthor(author);

            for(ParsedFileChange pFc : pc.fileChanges) {
                String path = pFc.newPath;
                if(pc.deletedFiles.contains(path)) {
                    path = null;
                }
                FileChange currentFileChange = new FileChange(0, 0, path, pFc.insertions, pFc.deletions);

                if(pFc.isRename()) {
                    fileTracker.changeName(pc.hash, pFc.oldPath, pFc.newPath);
                }
                File file = fileTracker.getFile(pc.hash, pFc.newPath);
                currentFileChange.setFile(file);
                fileChanges.add(currentFileChange);
            }
            for(String filePath : pc.deletedFiles) {
                fileTracker.onDeleteFile(pc.hash, filePath);
            }
            for(String filePath : pc.createdFiles) {
                fileTracker.onCreateFile(pc.hash, filePath);
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
        return unparsedCommits;
    }

    private static List<ParsedCommit> toParsedCommits(List<UnparsedCommit> unparsedCommits) throws ParseException {
        List<ParsedCommit> parsedCommits = new LinkedList<>();

        for (UnparsedCommit uc : unparsedCommits) {
            ParsedCommit pc = new ParsedCommit(uc.header);
            pc.fileChanges = uc.modifiedFiles.stream().map(ParsedFileChange::new).collect(Collectors.toList());
            pc.deletedFiles = uc.deletedFiles;
            pc.createdFiles = uc.createdFiles;
            parsedCommits.add(pc);
        }

        return parsedCommits;
    }

/*
    public static void parseLogStream(BufferedReader logInputStream) throws IOException, ParseException {
        Project project = new Project();
        project.setAuthors(new ArrayList<>());
        project.setCommits(new ArrayList<>());

        Map<String, Author> nameToAuthors = new HashMap<>();
        Map<String, File> pathToFileMap = new HashMap();
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        for (UnparsedCommit currentUnparsedCommit : unparsedCommits) {
            Scanner headerScanner = new Scanner(currentUnparsedCommit.header);
            String stringHash = headerScanner.findInLine("[0-9a-z]+");
            String stringAuthor = headerScanner.findInLine(" \\[.+?\\] ");
            stringAuthor = stringAuthor.substring(2, stringAuthor.length() - 2);
            String stringDate = headerScanner.findInLine("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
            String commitMessage = headerScanner.findInLine(".+").trim();
            String finalStringAuthor = stringAuthor;
            Author author = nameToAuthors.computeIfAbsent(stringAuthor, x -> new Author(0, 0, finalStringAuthor));
            Commit commit = new Commit(0, 0, stringHash, isMainBranch, 0, new Date(dateParser.parse(stringDate).getTime()), commitMessage);
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
                    String[] fileNames = getOldAndNewPath(fileName);
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

    private static String[] getOldAndNewPath(String renameString) {
        String pathPrefix = "";
        String pathSuffix = "";
        boolean isVeryLongRename = renameString.matches("(.+)?\\{.+=>.+}(.+)?");
        if(isVeryLongRename) {
            String[] bracketWrappers = renameString.split("\\{.+=>.+}", 2);
            pathPrefix = bracketWrappers[0];
            pathSuffix = bracketWrappers[1];
            renameString = renameString.substring(pathPrefix.length() + 1, renameString.length() - pathSuffix.length() - 1);
        }
        String[] renamedPart = renameString.split(" => ", 2);
        String oldPath = (pathPrefix + renamedPart[0] + pathSuffix).replace("//", "/");
        String newPath = (pathPrefix + renamedPart[1] + pathSuffix).replace("//", "/");
        return new String[]{
                oldPath,
                newPath
        };

    }

 */
}
