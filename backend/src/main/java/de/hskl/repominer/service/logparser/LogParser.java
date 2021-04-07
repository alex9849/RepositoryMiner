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
        double index = 0;
        List<ParsedCommit> parsedCommits = toParsedCommits(toUnparsedCommits(logInputStream));
        for(ParsedCommit pc : parsedCommits) {
            if(isNewestCommit) {
                fileTracker = new FileTracker(pc.hash);
                isNewestCommit = false;
            }
            System.out.println("Parsing commit: " + pc.hash + " (" + ++index / parsedCommits.size() * 100 + "%)");

            boolean isMerge = pc instanceof ParsedMergeCommit;

            List<FileChange> fileChanges = new LinkedList<>();
            Author author = nameToAuthorMap.computeIfAbsent(pc.author, x -> new Author(0, 0, pc.author));
            Commit currentCommit = new Commit(0, 0, pc.hash, isMerge, 0, pc.date, pc.commitMessage);
            currentCommit.setAuthor(author);

            if(!isMerge) {
                fileTracker.addCommit(pc);
            } else {
                fileTracker.addMerge((ParsedMergeCommit) pc);
            }

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

            fileTracker.afterParsingTasks(pc);

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
        List<ParsedMergeCommit> unsafeParentDiffMerges = new ArrayList<>();
        //GitTreeBuilder gitTreeBuilder = null;

        ParsedCommit currentParsedCommit = null;

        for (UnparsedCommit uc : unparsedCommits) {
            ParsedCommit nextCommit = ParsedCommit.parse(uc.header);
             boolean appendCommit = (currentParsedCommit instanceof ParsedMergeCommit)
                    && (nextCommit instanceof ParsedMergeCommit)
                    && nextCommit.hash.equals(currentParsedCommit.hash);
            if(!appendCommit) {
                /* If the next diff of a merge commit is not a commit we have to check if the parents
                   have been ordered correctly, because we can't surely tell if the diff came from the
                   source or the target parent */
                if(currentParsedCommit instanceof ParsedMergeCommit) {
                    ParsedMergeCommit mergeCommit = (ParsedMergeCommit) currentParsedCommit;
                    if(mergeCommit.changedFilesFromLeftTreeSide == null) {
                        boolean isUnsafe = false;
                        mergeCommit.changedFilesFromLeftTreeSide = new FileModificationHolder();
                        for(ParsedFileChange fileChange : mergeCommit.changedFiles.fileChanges) {
                            //We only have to double check mergecommits, is any files have been renamed
                            isUnsafe |= fileChange.isRename();
                        }
                        if(isUnsafe) {
                            unsafeParentDiffMerges.add(mergeCommit);
                        }
                    }
                }
                currentParsedCommit = nextCommit;
                parsedCommits.add(currentParsedCommit);

                /*if(gitTreeBuilder == null) {
                    gitTreeBuilder = new GitTreeBuilder(currentParsedCommit);
                } else {
                    gitTreeBuilder.append(currentParsedCommit);
                }*/
            }

            FileModificationHolder fmh = new FileModificationHolder();
            fmh.fileChanges = uc.modifiedFiles.stream().map(ParsedFileChange::new).collect(Collectors.toList());
            fmh.deletedFiles = uc.deletedFiles;
            fmh.createdFiles = uc.createdFiles;

            if(appendCommit) {
                ((ParsedMergeCommit) currentParsedCommit).changedFilesFromLeftTreeSide = fmh;
            } else {
                currentParsedCommit.changedFiles = fmh;
            }
        }
        /*if(gitTreeBuilder == null) {
            return parsedCommits;
        }

        GitTreeBuilder.GitTree gitTree = gitTreeBuilder.build();

        for(int i = unsafeParentDiffMerges.size() - 1; i >= 0; i--) {
            boolean isLeftSideDiff = false;
            ParsedMergeCommit checkMerge = unsafeParentDiffMerges.get(i);
            GitTreeBuilder.TreeNode currMergeNode = gitTree.getCommitNode(checkMerge.hash);
            ParsedFileChange searchRename = null;
            for(ParsedFileChange fc : checkMerge.changedFiles.fileChanges) {
                if(fc.isRename()) {
                    searchRename = fc;
                    break;
                }
            }
            if(searchRename == null) {
                checkMerge.changedFilesFromLeftTreeSide = new FileModificationHolder();
                continue;
            }

            GitTreeBuilder.TreeNode compareNode = currMergeNode;
            nodeComparisons:
            while (compareNode.getTargetParent() != null) {
                compareNode = compareNode.getTargetParent();
                for(ParsedFileChange fc : compareNode.getCommit().changedFiles.fileChanges) {
                    if(!fc.isRename()) {
                        continue;
                    }
                    if(fc.newPath.equals(searchRename.newPath)) {
                        isLeftSideDiff = true;
                        break nodeComparisons;
                    }
                }
            }

            if(isLeftSideDiff) {
                checkMerge.changedFilesFromLeftTreeSide = checkMerge.changedFiles;
                checkMerge.changedFiles = new FileModificationHolder();
            }
        }*/

        return parsedCommits;
    }

}
