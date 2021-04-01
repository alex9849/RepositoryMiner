package de.hskl.repominer.service.logparser;

import java.sql.Date;

public class ParsedMergeCommit extends ParsedCommit {
    FileModificationHolder changedFilesFromLeftTreeSide = new FileModificationHolder();
    //Das wo wir rein mergen. Das Y -> (YYYYY XXXXX)
    final String mergeSourceHash;

    protected ParsedMergeCommit(String hash, String parentHash, String author, Date date, String commitMessage,
                             String mergeSourceHash) {
        super(hash, parentHash, author, date, commitMessage);
        this.mergeSourceHash = mergeSourceHash;
    }

}
