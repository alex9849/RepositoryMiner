package de.hskl.repominer.service.logparser;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ParsedCommit {
    final String hash;
    final String parentHash;
    final String author;
    final Date date;
    final String commitMessage;

    FileModificationHolder changedFiles;

    protected ParsedCommit(String hash, String parentHash, String author, Date date, String commitMessage) {
        this.hash = hash;
        this.parentHash = parentHash;
        this.author = author;
        this.date = date;
        this.commitMessage = commitMessage;
    }

    public static ParsedCommit parse(String header) throws ParseException {
        Scanner headerScanner = new Scanner(header);
        String hash = headerScanner.findInLine("[0-9a-z]+");
        String stringAuthor = headerScanner.findInLine(" \\[.+?\\] ");
        String author = stringAuthor.substring(2, stringAuthor.length() - 2);
        String hashArea = headerScanner.findInLine("\\(([0-9a-z ]+)?\\)");
        hashArea = hashArea.substring(1, hashArea.length() - 1);

        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(dateParser.parse(headerScanner.findInLine("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d")).getTime());
        String commitMessage = headerScanner.findInLine(".+").trim();
        ParsedCommit parsedCommit;
        if(hashArea.contains(" ")) {
            String[] mergeTargetAndSource = hashArea.split(" ", 2);
            parsedCommit = new ParsedMergeCommit(hash, mergeTargetAndSource[0], author, date, commitMessage, mergeTargetAndSource[1]);
        } else {
            parsedCommit = new ParsedCommit(hash, hashArea, author, date, commitMessage);
        }
        return parsedCommit;
    }
}
