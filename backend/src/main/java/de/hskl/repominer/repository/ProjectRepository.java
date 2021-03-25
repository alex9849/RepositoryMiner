package de.hskl.repominer.repository;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectRepository {

    private final DataSource ds;

    public ProjectRepository(DataSource ds) {
        this.ds = ds;
    }

    public Project saveProject(Project project) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Project (name, lastUpdate) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, project.getName());
            pstmt.setDate(2, new Date(System.currentTimeMillis()));
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return loadProject(rs.getInt(1));
            }
            throw new DaoException("Error saving Project");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error saving Project");
        }
    }

    public List<Project> loadProjects() {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Project");
            ResultSet rs = pstmt.executeQuery();
            List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                projects.add(parseProject(rs));
            }
            return projects;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error loading Project");
        }
    }

    public Project loadProject(int id) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Project WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return parseProject(rs);
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error loading Project");
        }
    }

    private Project parseProject(ResultSet rs) throws SQLException {
        Project p = new Project(
                rs.getInt("id"),
                rs.getDate("lastUpdate")
        );
        p.setName(rs.getString("name"));
        return p;
    }
}
