package de.hskl.repominer.repository;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class AuthorRepository {

    private final DataSource ds;

    public AuthorRepository(DataSource ds) {
        this.ds = ds;
    }

    public Author loadAuthor(int id) {
        try (Connection con = ds.getConnection()) {
            return loadAuthor(id, con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error loading Author");
        }
    }

    Author loadAuthor(int id, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * from Author where id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return parseAuthor(rs);
        }
        return null;
    }

    public Author saveAuthor(Author author) {
        try (Connection con = ds.getConnection()) {
            return saveAuthor(author, con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error saving Author");
        }
    }

    Author saveAuthor(Author author, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Author (projectId, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, author.getProjectId());
        pstmt.setString(2, author.getName());
        pstmt.execute();
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return loadAuthor(rs.getInt(1), connection);
        }
        return null;
    }

    private Author parseAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getInt("id"),
                rs.getInt("projectId"),
                rs.getString("name")
        );
    }

}
