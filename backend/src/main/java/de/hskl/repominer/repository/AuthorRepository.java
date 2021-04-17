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
public class AuthorRepository {

    private final DataSource ds;

    public AuthorRepository(DataSource ds) {
        this.ds = ds;
    }

    public LogAuthor loadAuthor(int id) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * from Author where id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return parseAuthor(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Error loading Author", e);
        }
    }

    public List<LogAuthor> loadAllAuthorsForProject(int projectId) {
        List<LogAuthor> resultList = new ArrayList<>();

        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Author WHERE projectId = ?");
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                LogAuthor logAuthor = parseAuthor(rs);
                resultList.add(logAuthor);
            }

            return resultList;

        }catch(SQLException throwables){
            throw new DaoException("Error loading allAuthorsForPorject");
        }

    }


    public LogAuthor saveAuthor(LogAuthor logAuthor) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Author (projectId, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, logAuthor.getProjectId());
            pstmt.setString(2, logAuthor.getName());
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

    private LogAuthor parseAuthor(ResultSet rs) throws SQLException {
        return new LogAuthor(
                rs.getInt("projectId"),
                rs.getInt("id"),
                rs.getString("name")
        );
    }


}
