package de.hskl.repominer.repository;

import de.hskl.repominer.models.Commit;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class CommitRepository  {

    private final DataSource ds;

    public CommitRepository(DataSource ds) {
        this.ds = ds;
    }

    public Commit loadCommit(int id){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Commit\" WHERE id = ? ");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return parseCommit(rs);
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error loading Commit");
        }
    }


    public Commit saveCommit(Commit commit){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO \"Commit\" (projectId, hash, authorId,timestamp, message)" +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, commit.getProjectId());
            pstmt.setString(2, commit.getHash());
            pstmt.setInt(3, commit.getAuthorId());
            pstmt.setDate(4, commit.getTimeStamp());
            pstmt.setString(5, commit.getMessage());
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                return loadCommit(rs.getInt(1));
            }
            throw new DaoException("Error saving Commit");
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error saving Commit");
        }
    }

    public boolean deleteCommit(Commit commit){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM \"Commit\" WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, commit.getId());
            return pstmt.executeUpdate() != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error deleting Commit");
        }
    }

    private Commit parseCommit(ResultSet rs) throws SQLException {
        return new Commit(
                rs.getInt("id"),
                rs.getInt("projectId"),
                rs.getString("hash"),
                rs.getInt("authorId"),
                rs.getDate("timestamp"),
                rs.getString("message")
        );
    }


}
