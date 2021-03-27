package de.hskl.repominer.repository;

import de.hskl.repominer.models.File;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class FileRepository {

    private final DataSource ds;

    public FileRepository(DataSource ds) {
        this.ds = ds;
    }

    public File loadFile(int id) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM File WHERE id = ? ", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return parseFile(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Error loading File", e);
        }
    }

    public File saveFile(File file) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO File (projectId) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, file.getProjectId());
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                return loadFile(rs.getInt(1));
            }
            throw new DaoException("Error saving File");
        } catch (SQLException e) {
            throw new DaoException("Error saving File", e);
        }
    }

    public boolean deleteFile(File file) {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM File WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, file.getId());
            return pstmt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException("Error deleting File", e);
        }
    }

    private File parseFile(ResultSet rs) throws SQLException {
        return new File(
                rs.getInt("id"),
                rs.getInt("projectId")
        );
    }


}
