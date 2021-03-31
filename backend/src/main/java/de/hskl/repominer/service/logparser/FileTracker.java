package de.hskl.repominer.service.logparser;

import de.hskl.repominer.models.File;

import java.util.HashMap;
import java.util.Map;

public class FileTracker {
    private static class Branch implements Cloneable {
        final Map<String, File> pathToFileMap = new HashMap<>();

        public Branch clone() {
            Branch copy = new Branch();
            copy.pathToFileMap.putAll(pathToFileMap);
            return copy;
        }
    }

    private final Map<String, Branch> hashToBranchMap;


    public FileTracker(String lastCommitHash) {
        this.hashToBranchMap = new HashMap<>();
        this.hashToBranchMap.put(lastCommitHash, new Branch());
    }

    public void addCommit(ParsedCommit commit) {
        Branch childBranch = this.hashToBranchMap.get(commit.hash);
        boolean isSplitt = this.hashToBranchMap.containsKey(commit.parentHash);
        this.hashToBranchMap.put(commit.parentHash, childBranch);
        commit.changedFiles.fileChanges.forEach(x -> changeName(commit.hash, x.oldPath, x.newPath));
        //commit.changedFiles.deletedFiles.forEach(x -> onDeleteFile(commit.hash, x));
        commit.changedFiles.createdFiles.forEach(x -> onCreateFile(commit.hash, x));
    }

    public void addMerge(ParsedMergeCommit mergeCommit) {
        Branch targetBranch = this.hashToBranchMap.get(mergeCommit.hash);
        //Touch files
        mergeCommit.changedFiles.deletedFiles.forEach(x -> getFile(mergeCommit.hash, x));
        mergeCommit.changedFiles.fileChanges.forEach(x -> getFile(mergeCommit.hash, x.newPath));
        mergeCommit.changedFilesFromLeftTreeSide.deletedFiles.forEach(x -> getFile(mergeCommit.hash, x));
        mergeCommit.changedFilesFromLeftTreeSide.fileChanges.forEach(x -> getFile(mergeCommit.hash, x.newPath));
        Branch sourceBranch = targetBranch.clone();
        this.addCommit(mergeCommit);
        this.hashToBranchMap.put(mergeCommit.mergeSourceHash, sourceBranch);
        mergeCommit.changedFilesFromLeftTreeSide.fileChanges.forEach(x -> changeName(mergeCommit.mergeSourceHash, x.oldPath, x.newPath));
        //mergeCommit.changedFilesFromLeftTreeSide.deletedFiles.forEach(x -> onDeleteFile(mergeCommit.hash, x));
        mergeCommit.changedFilesFromLeftTreeSide.createdFiles.forEach(x -> onCreateFile(mergeCommit.mergeSourceHash, x));
    }

    /**
     * Returns the associated tracking Object if present.
     * Creates a tracking object otherwise and returns that
     */
    public File getFile(String commitHash, String path) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        return branch.pathToFileMap.computeIfAbsent(path, x -> new File(0, 0));
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
        this.hashToBranchMap.values().forEach(b -> b.pathToFileMap
                .entrySet().removeIf(x -> x.getValue() == file));
        return file;
    }
}
