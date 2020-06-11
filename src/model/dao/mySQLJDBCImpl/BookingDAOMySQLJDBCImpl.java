package model.dao.mySQLJDBCImpl;

import model.dao.BookingDAO;
import model.mo.Booking;
import model.mo.Structure;
import model.mo.User;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingDAOMySQLJDBCImpl implements BookingDAO {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public BookingDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ArrayList<Booking> findBookingsByDate(LocalDate date) {
        /**
         * Il metodo permette di cercare tutti gli appuntamenti presi in una determinata data <date>
         */
        ArrayList<Booking> bookings = new ArrayList<>();
        query = "SELECT * FROM BOOKING WHERE DATE = ? AND DELETED = 0 ORDER BY HOUR_START ASC;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setDate(i++, Date.valueOf(date));
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeQuery()");
            throw new RuntimeException(e);
        }
        try {
            while (rs.next()) {
                bookings.add(readBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
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

        return bookings;
    }

    private Booking readBooking(ResultSet rs) {

        /**
         * In questo metodo vado a settare tutti i campi di ogni colonna di BOOKING.
         */

        Booking booking = new Booking();
        Structure structure = new Structure();
        User customer = new User();
        booking.setStructure(structure);
        booking.setCustomer(customer);

        try {
            booking.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDeleted(rs.getBoolean("DELETED"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDeletedReason(rs.getString("DELETED_REASON"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"DELETED_REASON\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDate(rs.getObject("DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getDate(\"DATE\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setHourStart(rs.getTime("HOUR_START"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getTime(\"HOUR_START\")");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setId(rs.getLong("ID_CUSTOMER"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_CUSTOMER\")");
            throw new RuntimeException(e);
        }
        try {
            booking.getStructure().setId(rs.getLong("ID_STRUCTURE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_STRUCTURE\")");
            throw new RuntimeException(e);
        }

        return booking;
    }

}
