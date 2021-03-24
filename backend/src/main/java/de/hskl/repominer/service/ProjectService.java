package de.hskl.repominer.service;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.service.logparser.LogParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

@Service
public class ProjectService {

    public Project addProject(BufferedReader logInputStream) throws IOException, ParseException {
        //Todo implement
        InputStream stream = ProjectService.class.getClassLoader().getResourceAsStream("testLog.txt");

        Project project = LogParser.parseLogStream(new BufferedReader(new InputStreamReader(stream)));
        return null;
    }

}
