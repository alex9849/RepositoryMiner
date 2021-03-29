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

    public void addCommit(String commitHash, String parentHash) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        this.hashToBranchMap.put(parentHash, branch);
    }

    public void addMerge(String commitHash, String targetHash, String sourceHash) {
        this.addCommit(commitHash, targetHash);
        Branch targetBranch = this.hashToBranchMap.get(targetHash);
        this.hashToBranchMap.put(sourceHash, targetBranch.clone());
    }

    public File getFile(String commitHash, String path) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.get(path);
        if(file == null) {
            file = new File(0 ,0);
            final File addFile = file;
            this.hashToBranchMap.values().forEach(b -> b.pathToFileMap.put(path, addFile));
        }
        return file;
    }

    public File changeName(String commitHash, String oldPath, String newPath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(newPath);
        if(file == null) {
            file = getFile(commitHash, oldPath);
        }
        branch.pathToFileMap.put(oldPath, file);
        return file;
    }

    public File onDeleteFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        return branch.pathToFileMap.put(filePath, new File(0, 0));
    }

    public File onCreateFile(String commitHash, String filePath) {
        Branch branch = this.hashToBranchMap.get(commitHash);
        File file = branch.pathToFileMap.remove(filePath);
        //Wir haben die Datei hier erstellt. Also kann die fileID auf keinem anderem Branch existieren -> löschen
        this.hashToBranchMap.values().forEach(b -> b.pathToFileMap
                .entrySet().removeIf(x -> x.getValue() == file));
        return file;
    }
}
