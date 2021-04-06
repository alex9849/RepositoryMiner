package de.hskl.repominer.service.logparser;

import de.hskl.repominer.models.File;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileTracker {

    private static class Branch implements Cloneable {
        final Map<String, File> pathToFileMap = new HashMap<>();

        public Branch clone() {
            Branch copy = new Branch();
            copy.pathToFileMap.putAll(pathToFileMap);
            return copy;
        }
    }

    private final Set<File> deletedFiles;
    private final Map<String, Branch> hashToBranchMap;


    public FileTracker(String lastCommitHash) {
        this.hashToBranchMap = new HashMap<>();
        this.deletedFiles = new HashSet<>();
        Branch initBranch = new Branch();
        this.hashToBranchMap.put(lastCommitHash, initBranch);
    }

    public void afterParsingTasks(ParsedCommit commit) {
        afterParsingTasks(commit, !(commit instanceof ParsedMergeCommit));
    }

    private void afterParsingTasks(ParsedCommit commit, boolean doOnCreate) {
        if(doOnCreate) {
            commit.changedFiles.createdFiles.forEach(x -> onCreateFile(commit.hash, x));
        }
    }

    public void addCommit(ParsedCommit commit) {
        Branch childBranch = this.hashToBranchMap.get(commit.hash);
        boolean isSplitt = this.hashToBranchMap.containsKey(commit.parentHash);
        if(isSplitt) {
            Branch otherSplit = this.hashToBranchMap.get(commit.parentHash);
            childBranch.pathToFileMap.putAll(otherSplit.pathToFileMap);
            this.hashToBranchMap.forEach((key, value) -> {
                if (value == otherSplit) {
                    this.hashToBranchMap.put(key, childBranch);
                }
            });
        }
        this.hashToBranchMap.put(commit.parentHash, childBranch);
        commit.changedFiles.fileChanges.forEach(x -> getFile(commit.hash, x.newPath));
        commit.changedFiles.fileChanges.forEach(x -> changeName(commit.hash, x.oldPath, x.newPath));
    }

    public void addMerge(ParsedMergeCommit mergeCommit) {
        Branch targetBranch = this.hashToBranchMap.get(mergeCommit.hash);
        //Touch files
        Map<String, File> touchedFiles = new HashMap<>();
        mergeCommit.changedFiles.deletedFiles.forEach(x -> touchedFiles.put(x, getFile(mergeCommit.hash, x)));
        mergeCommit.changedFiles.fileChanges.forEach(x -> touchedFiles.put(x.newPath, getFile(mergeCommit.hash, x.newPath)));
        mergeCommit.changedFilesFromLeftTreeSide.deletedFiles.forEach(x -> touchedFiles.put(x, getFile(mergeCommit.hash, x)));
        mergeCommit.changedFilesFromLeftTreeSide.fileChanges.forEach(x -> touchedFiles.put(x.newPath, getFile(mergeCommit.hash, x.newPath)));
        Branch sourceBranch;
        if(this.hashToBranchMap.containsKey(mergeCommit.mergeSourceHash)) {
            sourceBranch = this.hashToBranchMap.get(mergeCommit.mergeSourceHash);
            touchedFiles.forEach((k, v) -> {
                if(sourceBranch.pathToFileMap.containsKey(k)) {
                    targetBranch.pathToFileMap.put(k , sourceBranch.pathToFileMap.get(k));
                } else {
                    sourceBranch.pathToFileMap.put(k, v);
                }
            });
        } else {
            sourceBranch = targetBranch.clone();
        }

        this.addCommit(mergeCommit);
        this.hashToBranchMap.put(mergeCommit.mergeSourceHash, sourceBranch);
        mergeCommit.changedFilesFromLeftTreeSide.fileChanges.forEach(x -> changeName(mergeCommit.mergeSourceHash, x.oldPath, x.newPath));
        //mergeCommit.changedFilesFromLeftTreeSide.deletedFiles.forEach(x -> onDeleteFile(mergeCommit.hash, x));
        //mergeCommit.changedFilesFromLeftTreeSide.createdFiles.forEach(x -> onCreateFile(mergeCommit.mergeSourceHash, x));
    }

    /**
     * Returns the associated tracking Object if present.
     * Creates a tracking object otherwise and returns that
     */
    public File getFile(String commitHash, String path) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        if(!branch.pathToFileMap.containsKey(path)
                || deletedFiles.contains(branch.pathToFileMap.get(path))) {
            File addFile = new File(0, 0);
            /*this.allBranches.forEach(x -> {
                x.pathToFileMap.put(path, addFile);
            });*/
            branch.pathToFileMap.put(path, addFile);
        }
        return branch.pathToFileMap.get(path);
    }

    private File changeName(String commitHash, String oldPath, String newPath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(newPath);
        if(file == null) {
            throw new NullPointerException("file is null");
            //file = branch.pathToFileMap.remove(newPath);
        }
        branch.pathToFileMap.put(oldPath, file);
        return file;
    }

    /*
    private File onDeleteFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        return branch.pathToFileMap.put(filePath, new File(0, 0));
    }
     */

    private File onCreateFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(filePath);
        //Wir haben die Datei hier erstellt. Also kann die fileID auf keinem anderem Branch existieren -> löschen
        //löschen wäre zu aufwendig. Also blacklist
        this.deletedFiles.add(file);
        /*this.hashToBranchMap.values().forEach(b -> b.pathToFileMap
                .entrySet().removeIf(x -> x.getValue() == file));*/
        return file;
    }
}
