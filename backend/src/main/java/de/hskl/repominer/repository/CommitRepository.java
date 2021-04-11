package de.hskl.repominer.repository;

import de.hskl.repominer.models.Commit;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        } catch (SQLException e) {
            throw new DaoException("Error loading Commit", e);
        }
    }

    public List<Commit> loadAllCommitsForProject(int projectId){
            List<Commit> resultList = new ArrayList<>();
        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM \"Commit\" WHERE projectId = ?");
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Commit c = parseCommit(rs);
                if(c != null) resultList.add(c);
            }

            return resultList;
        }catch(SQLException throwables){
            throw new DaoException("Error loading Commits for Project with id: " + projectId);
        }
    }


    public Commit saveCommit(Commit commit){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO \"Commit\" (projectId, isMerge, hash, authorId, timestamp, message)" +
                            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, commit.getProjectId());
            pstmt.setBoolean(2, commit.isMerge());
            pstmt.setString(3, commit.getHash());
            pstmt.setInt(4, commit.getAuthorId());
            pstmt.setDate(5, commit.getTimeStamp());
            pstmt.setString(6, commit.getMessage());
            pstmt.execute();

            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                return loadCommit(rs.getInt(1));
            }
            throw new DaoException("Error saving Commit");
        }catch (SQLException e){
            throw new DaoException("Error saving Commit", e);
        }
    }

    public boolean deleteCommit(Commit commit){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM \"Commit\" WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, commit.getId());
            return pstmt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting Commit", e);
        }
    }

    private Commit parseCommit(ResultSet rs) throws SQLException {
        return new Commit(
                rs.getInt("id"),
                rs.getInt("projectId"),
                rs.getString("hash"),
                rs.getBoolean("isMerge"),
                rs.getInt("authorId"),
                rs.getDate("timestamp"),
                rs.getString("message")
        );
    }


}
