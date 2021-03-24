package de.hskl.repominer.repository;

import de.hskl.repominer.models.Author;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private Author parseAuthor(ResultSet rs) throws SQLException {
        return new Author(
               rs.getInt("id"),
               rs.getInt("projectId"),
               rs.getString("name")
        );
    }

}
