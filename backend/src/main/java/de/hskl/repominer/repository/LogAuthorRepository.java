package de.hskl.repominer.repository;

import de.hskl.repominer.models.LogAuthor;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogAuthorRepository {
    private final DataSource ds;

    public LogAuthorRepository(DataSource ds) {
        this.ds = ds;
    }

    public List<LogAuthor> loadLogAuthorsForProject(int projectId) {
        List<LogAuthor> resultList = new ArrayList<>();

        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM LogAuthor WHERE projectId = ?");
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                LogAuthor author = parseLogAuthor(rs);
                resultList.add(author);
            }

            return resultList;

        } catch(SQLException throwables){
            throw new DaoException("Error loading allAuthorsForPorject");
        }
    }

    public LogAuthor addLogAuthor(LogAuthor logAuthor) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO LogAuthor (projectId, name, authorId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, logAuthor.getProjectId());
            pstmt.setString(2, logAuthor.getName());
            pstmt.setInt(3, logAuthor.getAuthorId());
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                logAuthor.setId(rs.getInt(1));
                return logAuthor;
            }
            throw new DaoException("Error saving Author");
        } catch (SQLException e) {
            throw new DaoException("Error saving Author", e);
        }
    }

    public LogAuthor updateLogAuthor(Integer authorId, LogAuthor logAuthor){
        String updateStmt = "   UPDATE LogAuthor" +
                "               SET AuthorId = ? " +
                "               WHERE id = ?";

        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement(updateStmt);
            pstmt.setObject(1, authorId);
            pstmt.setInt(2, logAuthor.getId());
            pstmt.execute();

        }catch(SQLException throwables){
            throw new DaoException("Error updating LogAuthor");
        }

        return logAuthor;
    }

    private LogAuthor parseLogAuthor(ResultSet rs) throws SQLException {
        return new LogAuthor(
                rs.getInt("projectId"),
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("authorId")
        );
    }
}
