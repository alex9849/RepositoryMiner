package de.hskl.repominer.repository;

import de.hskl.repominer.models.CurrentPath;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentPathRepository {

    private final DataSource ds;

    public CurrentPathRepository(DataSource ds) {
        this.ds = ds;
    }

    public List<CurrentPath> getAllCurrentPaths(int projectId) {
        List<CurrentPath> pathList = new ArrayList<>();

        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement pstmt = con.prepareStatement("SELECT projectId, fileId, hash, path " +
                    "From CurrentPath WHERE projectId = ?");
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();


            while(rs.next()){
                CurrentPath cp = parseCurrentPath(rs);
                pathList.add(cp);
            }

            return pathList;

        }catch (SQLException ex) {
            throw new DaoException("Error loading CurrentPath");
        }
    }

    public CurrentPath parseCurrentPath(ResultSet rs) throws SQLException {
        return new CurrentPath(
                rs.getInt("fileId"),
                rs.getString("hash"),
                rs.getString("path")
        );
    }
}
