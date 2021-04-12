package de.hskl.repominer.repository;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.chart.datagetter.OwnerShip;
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
        } catch (SQLException e) {
            throw new DaoException("Error saving Project", e);
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
        } catch (SQLException e) {
            throw new DaoException("Error loading Project", e);
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
        } catch (SQLException e) {
            throw new DaoException("Error loading Project", e);
        }
    }

    public boolean deleteProject(int projectId) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM Project WHERE id = ?");
            pstmt.setInt(1, projectId);
            return pstmt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting Project", e);
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

    public List<OwnerShip> getOwnerShip(int projectId, String path) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT a.name as author, " +
                    "sum(fc.insertions) as insertions, sum(fc.deletions) as deletions " +
                    "from CurrentPath cp " +
                    "    join FileChange fc on fc.fileId = cp.fileId " +
                    "    join \"Commit\" c on fc.commitId = c.id " +
                    "    join Author a on a.id = c.authorId " +
                    "where cp.projectId = ? and cp.path like (? || '%') " +
                    "group by a.id " +
                    "order by insertions + deletions desc");
            pstmt.setInt(1, projectId);
            pstmt.setString(2, path);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            List<OwnerShip> ownerShips = new ArrayList<>();
            while (rs.next()) {
                OwnerShip ownerShip = new OwnerShip();
                ownerShip.setAuthorName(rs.getString("author"));
                ownerShip.setInsertions(rs.getInt("insertions"));
                ownerShip.setDeletions(rs.getInt("deletions"));
                ownerShips.add(ownerShip);
            }
            return ownerShips;
        } catch (SQLException e) {
            throw new DaoException("Error calculating ownership", e);
        }
    }
}
