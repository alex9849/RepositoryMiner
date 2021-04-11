package de.hskl.repominer.repository;

import de.hskl.repominer.models.Author;
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

    public Author loadAuthor(int id) {
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

    public List<Author> loadAllAuthorsForProject(int projectId) {
        List<Author> resultList = new ArrayList<>();

        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Author WHERE projectId = ?");
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Author author = parseAuthor(rs);
                resultList.add(author);
            }

            return resultList;

        }catch(SQLException throwables){
            throw new DaoException("Error loading allAuthorsForPorject");
        }

    }


    public Author saveAuthor(Author author) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Author (projectId, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, author.getProjectId());
            pstmt.setString(2, author.getName());
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return loadAuthor(rs.getInt(1));
            }
            throw new DaoException("Error saving Author");
        } catch (SQLException e) {
            throw new DaoException("Error saving Author", e);
        }
    }

    private Author parseAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getInt("projectId"),
                rs.getInt("id"),
                rs.getString("name")
        );
    }


}
