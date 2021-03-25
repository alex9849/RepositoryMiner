package de.hskl.repominer.repository;

import de.hskl.repominer.models.Commit;
import de.hskl.repominer.models.File;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;

@Service
public class FileRepository {

    private final DataSource ds;

    public FileRepository(DataSource ds){
        this.ds = ds;
    }

    public File loadFile(int id ){
        try (Connection con = ds.getConnection()){
            return loadFile(id, con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Error loading File");
        }
    }

    File loadFile(int id, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM File WHERE id = ? ", Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1,id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return parseFile(rs);
        }
        return null;
    }

    public File saveFile(File file){
        try (Connection con = ds.getConnection()) {
            return saveFile(file, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error saving File");
        }
    }

    File saveFile(File file, Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO File (projectId) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, file.getProjectId());
        pstmt.execute();
        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return loadFile(rs.getInt(1), connection);
        }
        return null;
    }

    File deleteFile(File file){
        try (Connection con = ds.getConnection()){
            return deleteFile(file, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error deleting File");
        }
    }

    private File deleteFile(File file, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("DELETE FROM File WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, file.getId());
        pstmt.execute();
        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()){
            return file;
        }
        return null;
    }


    private File parseFile(ResultSet rs) throws SQLException {
        return new File(
                rs.getInt("id"),
                rs.getInt("projectId")
        );
    }


}
