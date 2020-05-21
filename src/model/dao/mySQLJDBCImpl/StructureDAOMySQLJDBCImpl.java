package model.dao.mySQLJDBCImpl;

import model.dao.StructureDAO;
import model.mo.Admin;
import model.mo.Structure;
import model.mo.User;
import services.logservice.LogService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StructureDAOMySQLJDBCImpl implements StructureDAO {


    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;
    Logger logger = LogService.getApplicationLogger();


    public StructureDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Structure fetchStructure() {
        Structure structure = new Structure();
        query = "SELECT * FROM STRUCTURE WHERE ID = 1;";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
           System.err.println("prima di rs.next()");
            if (rs.next()) { /* Esiste una struttura con quell'ID */
                structure = readStructure(rs);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella structure = readStructure(rs);");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close()");
            throw new RuntimeException(e);
        }

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        return structure;
    }

    private Structure readStructure(ResultSet rs) {

        Structure structure = new Structure();
        Admin admin = new Admin();
        User user = new User();
        admin.setUser(user);
        structure.setAdmin(admin);

        try {
            structure.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setAddress(rs.getString("ADDRESS"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PRODUCER\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setOpeningTime(rs.getString("OPENING_TIME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"OPENING_TIME\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setClosingTime(rs.getString("CLOSING_TIME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"CLOSING_TIME\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setSlot(rs.getString("SLOT"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"SLOT\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"NAME\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setPhone(rs.getString("PHONE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PHONE\")");
            throw new RuntimeException(e);
        }
        try {
            structure.getAdmin().getUser().setId(rs.getLong("ID_ADMIN")); /*TODO : da controllare*/
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"SHOWCASE\")");
            throw new RuntimeException(e);
        }

        return structure;
    }
}
