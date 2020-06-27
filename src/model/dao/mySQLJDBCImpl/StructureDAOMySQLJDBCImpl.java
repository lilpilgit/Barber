package model.dao.mySQLJDBCImpl;

import model.dao.StructureDAO;
import model.mo.Structure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StructureDAOMySQLJDBCImpl implements StructureDAO {


    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public StructureDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Structure fetchStructure() {
        Structure structure = new Structure();
        query =
                "SELECT *"
              + " FROM STRUCTURE"
              + " WHERE ID = 1;"; /* PERCHÉ IN TALE APPLICATIVO STIAMO TRATTANDO UN UNICA STRUTTURA */
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

    @Override
    public boolean update(Structure structure) {
        /**
         * Update infos about UNIQUE structure in DB
         *
         * @return Return true if the object is updated correctly in the DB otherwise raise an exception.
         * */

        /* Aggiorniamo i dati di structure */
        query
                = " UPDATE STRUCTURE S"
                + " SET "
                + "  ADDRESS = ?,"
                + "  OPENING_TIME = ?,"
                + "  CLOSING_TIME = ?,"
                + "  SLOT = ?,"
                + "  NAME = ?,"
                + "  PHONE = ?"
                + " WHERE"
                + "  S.ID = ?;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, structure.getAddress());
            ps.setTime(i++, structure.getOpeningTime());
            ps.setTime(i++, structure.getClosingTime());
            ps.setTime(i++, structure.getSlot());
            ps.setString(i++, structure.getName());
            ps.setString(i++, structure.getPhone());
            ps.setLong(i++,structure.getId());

        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }

        /*Chiudo il preparedStatement*/
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /* se non è stata sollevata alcuna eccezione fin qui, ritorno true perché significa
         * che l'aggiornamento di STRUCTURE è andato a buon fine */
        return true;
    }

    private Structure readStructure(ResultSet rs) {

        Structure structure = new Structure();

        try {
            structure.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            structure.setAddress(rs.getString("ADDRESS"));
        } catch (SQLException e) {
            System.err.println("Errore nella structure.setAddress(rs.getString(\"ADDRESS\"));");
            throw new RuntimeException(e);
        }
        try {
            structure.setOpeningTime(rs.getTime("OPENING_TIME"));
        } catch (SQLException e) {
            System.err.println("structure.setOpeningTime(rs.getTime(\"OPENING_TIME\"));");
            throw new RuntimeException(e);
        }
        try {
            structure.setClosingTime(rs.getTime("CLOSING_TIME"));
        } catch (SQLException e) {
            System.err.println("Errore nella structure.setClosingTime(rs.getTime(\"CLOSING_TIME\").toInstant());");
            throw new RuntimeException(e);
        }
        try {
                structure.setSlot(rs.getTime("SLOT"));
        } catch (SQLException e) {
            System.err.println("Errore nella structure.setSlot(rs.getTime(\"SLOT\").toInstant());");
            throw new RuntimeException(e);
        }
        try {
            structure.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella structure.setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            structure.setPhone(rs.getString("PHONE"));
        } catch (SQLException e) {
            System.err.println("Errore nella structure.setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }

        return structure;
    }
}
