package de.hskl.repominer.service.logparser;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ParsedCommit {
    String hash;
    String parentHash;
    String author;
    Date date;
    String commitMessage;

    boolean isMerge;
    //Das wo wir rein mergen. Das Y -> (YYYYY XXXXX)
    String mergeSourceHash;
    List<ParsedFileChange> fileChanges;
    Set<String> createdFiles;
    Set<String> deletedFiles;


    ParsedCommit(String header) throws ParseException {
        Scanner headerScanner = new Scanner(header);
        this.hash = headerScanner.findInLine("[0-9a-z]+");
        String stringAuthor = headerScanner.findInLine(" \\[.+?\\] ");
        this.author = stringAuthor.substring(2, stringAuthor.length() - 2);
        String hashArea = headerScanner.findInLine("\\(([0-9a-z ]+)?\\)");
        hashArea = hashArea.substring(1, hashArea.length() - 1);
        if(hashArea.contains(" ")) {
            String[] mergeTargetAndSource = hashArea.split(" ", 2);
            this.parentHash = mergeTargetAndSource[0];
            this.mergeSourceHash = mergeTargetAndSource[1];
            this.isMerge = true;
        } else {
            this.parentHash = hashArea;
            this.isMerge = false;
        }
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = new Date(dateParser.parse(headerScanner.findInLine("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d")).getTime());
        this.commitMessage = headerScanner.findInLine(".+").trim();
    }
}
