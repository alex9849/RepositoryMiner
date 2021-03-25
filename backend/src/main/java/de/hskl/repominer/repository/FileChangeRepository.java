package de.hskl.repominer.repository;

import de.hskl.repominer.models.FileChange;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;


@Service
public class FileChangeRepository {

    private final DataSource ds;

    public FileChangeRepository(DataSource ds){
        this.ds = ds;
    }

    public FileChange loadFileChange(int commitId, int fileId){
        try (Connection con = ds.getConnection()){
            return loadFileChange(commitId, fileId, con);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error loading FileChange");
        }
    }

    private FileChange loadFileChange(int commitId, int fileId, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM FileChange WHERE commitId = ? AND fileId = ?");
        pstmt.setInt(1, commitId);
        pstmt.setInt(2, fileId);
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            return parseFileChange(rs);
        }
        return null;
    }

    public FileChange saveFileChange(FileChange fileChange) {
        try(Connection con = ds.getConnection()){
            return saveFileChange(fileChange, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Errror saving FileChange");
        }
    }

    FileChange saveFileChange(FileChange fileChange, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO FileChange (commitId, fileId, path, insertions, deletions)"
                + "VALUES (?, ?, ?, ?, ?)");
        pstmt.setInt(1, fileChange.getCommitId());
        pstmt.setInt(2, fileChange.getFileId());
        pstmt.setString(3, fileChange.getPath());
        pstmt.setInt(4, fileChange.getInsertions());
        pstmt.setInt(5, fileChange.getDeletions());
        pstmt.execute();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return loadFileChange(fileChange.getCommitId(), fileChange.getFileId(), con);
        }
        return null;
    }

    public void deleteFileChange(int commitId, int fileId){
        try (Connection con = ds.getConnection()){
            deleteFileChange(commitId, fileId, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error deleting FileChange");
        }
    }

    private void deleteFileChange(int commitId, int fileId, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("DELETE FROM FileChange WHERE commitId = ? AND fileId = ?",
                Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, commitId);
        pstmt.setInt(2, fileId);
        pstmt.execute();
    }


    public FileChange parseFileChange(ResultSet rs) throws SQLException {
        return new FileChange(
                rs.getInt("commitId"),
                rs.getInt("fileId"),
                rs.getString("path"),
                rs.getInt("insertions"),
                rs.getInt("deletions")
        );
    }

}
