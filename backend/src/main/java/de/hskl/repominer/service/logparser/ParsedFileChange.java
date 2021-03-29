package de.hskl.repominer.service.logparser;

import java.util.Objects;
import java.util.Scanner;

public class ParsedFileChange {
    String oldPath;
    String newPath;
    int insertions;
    int deletions;

    public ParsedFileChange(String fileChangeText) {
        Scanner fileModifyScanner = new Scanner(fileChangeText);
        this.insertions = 0;
        if(fileModifyScanner.hasNextInt()) {
            this.insertions = fileModifyScanner.nextInt();
        } else {
            fileModifyScanner.next();
        }
        this.deletions = 0;
        if(fileModifyScanner.hasNextInt()) {
            this.deletions = fileModifyScanner.nextInt();
        } else {
            fileModifyScanner.next();
        }
        String fileName = fileModifyScanner.findInLine(".+").trim();
        if(fileName.matches(".+ =\\> .+")) {
            String[] fileNames = getOldAndNewPath(fileName);
            this.oldPath = fileNames[0];
            this.newPath = fileNames[1];
        } else {
            this.oldPath = fileName;
            this.newPath = fileName;
        }
    }

    boolean isRename() {
        return !Objects.equals(oldPath, newPath);
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
}
