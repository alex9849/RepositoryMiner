package de.hskl.repominer.repository;

import de.hskl.repominer.models.FileChange;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class FileChangeRepository {

    private final DataSource ds;

    public FileChangeRepository(DataSource ds){
        this.ds = ds;
    }

    public FileChange loadFileChange(int commitId, int fileId){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM FileChange WHERE commitId = ? AND fileId = ?");
            pstmt.setInt(1, commitId);
            pstmt.setInt(2, fileId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return parseFileChange(rs);
            }
            return null;
        }catch (SQLException e){
            throw new DaoException("Error loading FileChange", e);
        }
    }

    public List<FileChange> loadAllFileChangesByFileId(int fileId){
        List<FileChange> resultList = new ArrayList<>();
        try{
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM FileChange WHERE fileId = ?");
            pstmt.setInt(1, fileId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                FileChange fc = parseFileChange(rs);
                if( fc != null )
                    resultList.add(fc);
            }

            return resultList;
        } catch (SQLException throwables) {
            throw new DaoException("Error loading FileChanges");
        }
    }

    public FileChange saveFileChange(FileChange fileChange) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
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
                return loadFileChange(fileChange.getCommitId(), fileChange.getFileId());
            }
            throw new DaoException("Errror saving FileChange");
        } catch(SQLException e){
            throw new DaoException("Errror saving FileChange", e);
        }
    }

    public boolean deleteFileChange(int commitId, int fileId){
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM FileChange WHERE commitId = ? AND fileId = ?",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, commitId);
            pstmt.setInt(2, fileId);
            return pstmt.executeUpdate() != 0;
        }catch(SQLException e){
            throw new DaoException("Error deleting FileChange", e);
        }
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
