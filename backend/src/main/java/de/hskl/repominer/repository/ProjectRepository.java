package de.hskl.repominer.repository;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.chart.datagetter.OwnerShip;
import de.hskl.repominer.models.chart.datagetter.commitmap.FileCommitMatrix;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectRepository {

    private final DataSource ds;

    public ProjectRepository(DataSource ds) {
        this.ds = ds;
    }

    public Project addProject(Project project) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Project (name, lastUpdate) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, project.getName());
            pstmt.setDate(2, new Date(System.currentTimeMillis()));
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
                return project;
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
                    "total(fc.insertions) as insertions, total(fc.deletions) as deletions " +
                    "from CurrentPath cp " +
                    "    join FileChange fc on fc.fileId = cp.fileId " +
                    "    join \"Commit\" c on fc.commitId = c.id " +
                    "    join LogAuthor la on la.id = c.authorId " +
                    "    join Author a on a.id = la.authorId " +
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
                ownerShip.setPath(path);
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

    public List<OwnerShip> getOwnerShipForFolder(int projectId, String folderPath) {
        if(!folderPath.isEmpty()) {
            folderPath += "/";
        }
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT substr(folderContent.path, length(?) + 1) as path,\n" +
                    "       a.name               as author,\n" +
                    "       total(fc.insertions) as insertions,\n" +
                    "       total(fc.deletions)  as deletions\n" +
                    "from (select substr(path, 1, case\n" +
                    "                                 when instr(substr(path, length(?) + 1), '/') != 0\n" +
                    "                                     then length(?) + instr(substr(path, length(?) + 1), '/')\n" +
                    "                                 else length(?) + 1 +\n" +
                    "                                      length(substr(path, length(?) + 1)) end) as path\n" +
                    "      from CurrentPath\n" +
                    "      where path like ? || '%'\n" +
                    "        and projectId = ?\n" +
                    "      group by substr(path, length(?) + 1, case\n" +
                    "                                                            when instr(substr(path, length(?) + 1), '/') != 0\n" +
                    "                                                                then instr(substr(path, length(?) + 1), '/')\n" +
                    "                                                            else length(substr(path, length(?) + 1)) end)) folderContent\n" +
                    "         join CurrentPath cp on cp.path like folderContent.path || '%'\n" +
                    "         join FileChange fc on fc.fileId = cp.fileId\n" +
                    "         join \"Commit\" c on fc.commitId = c.id\n" +
                    "         join LogAuthor la on la.id = c.authorId " +
                    "         join Author a on a.id = la.authorId " +
                    "where cp.projectId = ?\n" +
                    "group by a.id, folderContent.path\n" +
                    "order by folderContent.path desc, a.name asc");
            pstmt.setString(1, folderPath);
            pstmt.setString(2, folderPath);
            pstmt.setString(3, folderPath);
            pstmt.setString(4, folderPath);
            pstmt.setString(5, folderPath);
            pstmt.setString(6, folderPath);
            pstmt.setString(7, folderPath);
            pstmt.setInt(8, projectId);
            pstmt.setString(9, folderPath);
            pstmt.setString(10, folderPath);
            pstmt.setString(11, folderPath);
            pstmt.setString(12, folderPath);
            pstmt.setInt(13, projectId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            List<OwnerShip> ownerShips = new ArrayList<>();
            while (rs.next()) {
                OwnerShip ownerShip = new OwnerShip();
                ownerShip.setAuthorName(rs.getString("author"));
                ownerShip.setPath(rs.getString("path"));
                ownerShip.setInsertions(rs.getInt("insertions"));
                ownerShip.setDeletions(rs.getInt("deletions"));
                ownerShips.add(ownerShip);
            }
            return ownerShips;
        } catch (SQLException e) {
            throw new DaoException("Error calculating ownership", e);
        }
    }

    public List<OwnerShip> getOwnerShipDevelopment(int projectId, String path) {
        String query = "WITH RECURSIVE dates(date) AS (\n" +
                "    VALUES ((SELECT min(date(timestamp / 1000, 'unixepoch', 'localtime', '+1 day')) from \"Commit\" where projectId = ?))\n" +
                "    UNION ALL\n" +
                "    SELECT date(date, '+1 day')\n" +
                "    FROM dates\n" +
                "    WHERE date < (SELECT max(date(timestamp / 1000, 'unixepoch', 'localtime', '+1 day')) from \"Commit\" where projectId = ?)\n" +
                ")\n" +
                "SELECT CAST(strftime('%s', d.date, 'localtime') AS INT) * 1000 as date, a.name as author, " +
                "total(case when cp.path is null then 0 else fc.insertions end) as insertions, " +
                "total(case when cp.path is null then 0 else fc.deletions end) as deletions\n" +
                "from dates d\n" +
                "         join Author a on a.projectId = ?\n" +
                "         join LogAuthor la on a.id = la.authorId\n" +
                "         left join \"Commit\" c on d.date >= date(c.timestamp / 1000, 'unixepoch', 'localtime') AND c.authorId = la.id\n" +
                "         left join FileChange fc on fc.commitId = c.id\n" +
                "         left join CurrentPath cp on fc.fileId = cp.fileId\n" +
                "where cp.path like (? || '%') or cp.path is null\n" +
                "group by d.date, a.id\n" +
                "order by d.date asc, a.name asc";
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, projectId);
            pstmt.setInt(2, projectId);
            pstmt.setInt(3, projectId);
            pstmt.setString(4, path);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            List<OwnerShip> ownerShips = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            while (rs.next()) {
                OwnerShip ownerShip = new OwnerShip();
                ownerShip.setPath(path);
                ownerShip.setAuthorName(rs.getString("author"));
                ownerShip.setInsertions(rs.getInt("insertions"));
                ownerShip.setDeletions(rs.getInt("deletions"));
                ownerShip.setDate(rs.getDate("date"));
                ownerShips.add(ownerShip);
            }
            return ownerShips;
        } catch (SQLException  e) {
            throw new DaoException("Error calculating ownership", e);
        }
    }

    public FileCommitMatrix getFileCommitMatrix(int projectId, String path, FileCommitMatrix.Sorting sortBy) {
        String query = "select cp.path, c.hash\n" +
                "from CurrentPath cp\n" +
                "         left join FileChange fc on cp.fileId = fc.fileId\n" +
                "         left join \"Commit\" c on fc.commitId = c.id\n" +
                "where cp.path LIKE ? || '%'\n" +
                "  AND cp.projectId = ? ";
        if(sortBy == FileCommitMatrix.Sorting.BY_NAME) {
            query += "ORDER BY cp.path DESC";
        } else {
            query += "ORDER BY c.timestamp DESC";
        }
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, path);
            pstmt.setInt(2, projectId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            Map<String, Integer> fileIndex = new HashMap<>();
            Map<String, Integer> commitIndex = new HashMap<>();
            Map<String, Set<String>> fileWithCommits = new HashMap<>();
            while (rs.next()) {
                fileIndex.computeIfAbsent(rs.getString("path"), x -> fileIndex.size());
                commitIndex.computeIfAbsent(rs.getString("hash"), x -> commitIndex.size());
                fileWithCommits.computeIfAbsent(rs.getString("path"), x -> new HashSet<>())
                        .add(rs.getString("hash"));
            }
            FileCommitMatrix matrix = new FileCommitMatrix(fileIndex.size(), commitIndex.size());
            for(Map.Entry<String, Integer> fileEntry : fileIndex.entrySet()) {
                matrix.setYContext(fileEntry.getValue(), fileEntry.getKey());
                for(Map.Entry<String, Integer> commitEntry : commitIndex.entrySet()) {
                    matrix.setXContext(commitEntry.getValue(), commitEntry.getKey());
                    if(fileWithCommits.get(fileEntry.getKey()).contains(commitEntry.getKey())) {
                        matrix.setValue(fileEntry.getValue(), commitEntry.getValue(), 1);
                    }
                }
            }

            return matrix;
        } catch (SQLException  e) {
            throw new DaoException("Error calculating ownership", e);
        }
    }
}
