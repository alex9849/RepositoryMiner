package de.hskl.repominer.repository;

import de.hskl.repominer.models.FileChange;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;


@Service
public class FileChangeRepository {

    private final DataSource ds;

    public FileChangeRepository(DataSource ds){
        this.ds = ds;
    }

    public FileChange loadFileChange(int fileId, String hash){
        try (Connection con = ds.getConnection()){
            return loadFileChange(fileId, hash, con);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error loading FileChange");
        }
    }

    private FileChange loadFileChange(int fileId, String hash, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM FileChange WHERE commitHash = ? AND fileId = ?");
        pstmt.setString(1, hash);
        pstmt.setInt(2,fileId);
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            return parseFileChange(rs);
        }
        return null;
    }

    FileChange saveFileChange(FileChange fileChange) {
        try(Connection con = ds.getConnection()){
            return saveFileChange(fileChange, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Errror saving FileChange");
        }
    }

    private FileChange saveFileChange(FileChange fileChange, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO FileChange (commitHash, fileId, path, insertions, deletions)"
                + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, fileChange.getCommitHash());
        pstmt.setInt(2, fileChange.getFileId());
        pstmt.setString(3, fileChange.getPath());
        pstmt.setInt(4, fileChange.getInsertions());
        pstmt.setInt(5, fileChange.getDeletions());
        pstmt.execute();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return fileChange;
        }
        return null;
    }

    public FileChange deleteFileChange(FileChange fileChange){
        try (Connection con = ds.getConnection()){
            return deleteFileChange(fileChange, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error deleting FileChange");
        }
    }

    private FileChange deleteFileChange(FileChange fileChange, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("DELETE FROM FileChange WHERE commitHash = ? AND fileId = ?", Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, fileChange.getCommitHash());
        pstmt.setInt(2, fileChange.getFileId());
        pstmt.execute();

        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return fileChange;
        }
        return  null;
    }


    public FileChange parseFileChange(ResultSet rs) throws SQLException {
        return new FileChange(
                rs.getString("commitHash"),
                rs.getInt("fileId"),
                rs.getString("path"),
                rs.getInt("insertions"),
                rs.getInt("deletions")
        );
    }

}
