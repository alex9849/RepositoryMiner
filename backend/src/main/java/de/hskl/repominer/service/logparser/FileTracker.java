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
        this.hashToBranchMap.put(commit.parentHash, childBranch);

        for(ParsedFileChange fc : commit.changedFiles.fileChanges) {

        }
    }

    public void addMerge(ParsedMergeCommit mergeCommit) {
        this.addCommit(mergeCommit);
        Branch targetBranch = this.hashToBranchMap.get(mergeCommit.parentHash);
        Branch sourceBranch = targetBranch.clone();
        this.hashToBranchMap.put(mergeCommit.mergeSourceHash, sourceBranch);
    }

    public File getFile(String commitHash, String path) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.get(path);
        if(file == null) {
            file = new File(0 ,0);
            final File addFile = file;
            /*  Wir legen diese Datei in jedem Branch an.
                Falls wir in die Date in einem anderem Branch sehen muss diese gemergt worden sein.
                Der Fall, dass die Datei auf dem einen Branch bearbeitet und auf den anderem gelöscht wurde
                führt zum merge-conflict und muss deshalb nicht beachtet werden
             */
            this.hashToBranchMap.values().forEach(b -> b.pathToFileMap.put(path, addFile));
        }
        return file;
    }

    private File changeName(String commitHash, String oldPath, String newPath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(newPath);
        if(file == null) {
            /*  Generiere neue FileId auf allen branches
                Beachte Scenario 3. Die Datei kann auf den anderen branches nicht anders heißen, da dies zu
                einem mergeconflict geführt hätte. Sollte die Datei auf einem anderen branch bearbeitet worden sein
                muss diese vorher auch umbenannt worden sein
             */
            getFile(commitHash, newPath);
            file = branch.pathToFileMap.remove(newPath);
        }
        branch.pathToFileMap.put(oldPath, file);
        return file;
    }

    private File onDeleteFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        return branch.pathToFileMap.put(filePath, new File(0, 0));
    }

    private File onCreateFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(filePath);
        //Wir haben die Datei hier erstellt. Also kann die fileID auf keinem anderem Branch existieren -> löschen
        this.hashToBranchMap.values().forEach(b -> b.pathToFileMap
                .entrySet().removeIf(x -> x.getValue() == file));
        return file;
    }
}
