package de.hskl.repominer.repository;

import de.hskl.repominer.models.Project;
import de.hskl.repominer.models.exception.DaoException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ProjectRepository {

    private final DataSource ds;

    public ProjectRepository(DataSource ds){
        this.ds = ds;
    }

    public Project loadProject(int id){
        try (Connection con = ds.getConnection()){
            return loadProject(id, con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DaoException("Error loading Project");
        }
    }

    Project loadProject(int id, Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Project WHERE id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return parseProject(rs);
        }
        return null;
    }

    private Project parseProject(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getDate("lastUpdate")
        );
    }
}
