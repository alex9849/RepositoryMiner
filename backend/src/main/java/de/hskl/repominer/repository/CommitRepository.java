package de.hskl.repominer.repository;

import de.hskl.repominer.models.Commit;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class CommitRepository  {

    private final DataSource ds;

    public CommitRepository(DataSource ds) {
        this.ds = ds;
    }

    public Commit loadCommit(String hash ){
        try (Connection con = ds.getConnection()){
            return loadCommit(hash, con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error laoding Commit");
        }
    }

   Commit loadCommit(String hash, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Commit WHERE hash = ? ");
        pstmt.setString(1,hash);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return parseCommit(rs);
        }
        return null;
    }


    public Commit saveCommit(Commit commit){
        try (Connection con = ds.getConnection()){
            return saveCommit(commit, con);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error saving Commit");
        }
    }

    Commit saveCommit(Commit commit, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO Commit (projectId, hash, authorId,timestamp, message)" +
                    "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, commit.getProjectId());
        pstmt.setString(2, commit.getHash());
        pstmt.setInt(3, commit.getAuthorId());
        pstmt.setDate(4, commit.getTimeStamp());
        pstmt.setString(5, commit.getMessage());
        pstmt.execute();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return loadCommit(rs.getString(2));
        }
        return null;

    }

    public Commit deleteCommit(Commit commit){
        try(Connection con = ds.getConnection()) {
            return deleteCommit(commit, con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error deleting Commit");
        }
    }

    private Commit deleteCommit(Commit commit, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Commit WHERE hash = ?", Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, commit.getHash());
        pstmt.execute();
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()){
            return commit;
        }
        return null;
    }

    private Commit parseCommit(ResultSet rs) throws SQLException {
        return new Commit(
                rs.getInt("projectId"),
                rs.getString("hash"),
                rs.getInt("authorId"),
                rs.getDate("timestamp"),
                rs.getString("message")
        );
    }


}
