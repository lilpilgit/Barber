package model.dao.mySQLJDBCImpl;

import model.dao.BookingDAO;
import model.mo.Booking;


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
        ArrayList<Booking> bookings = new ArrayList<>();
        query = "SELECT * FROM BOOKING WHERE DATE = ? ORDER BY HOUR_START ASC;";
        try {
            ps = connection.prepareStatement(query);
            ps.setDate(1, Date.valueOf(date));
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
/*
                bookings.add(readBooking(rs));
*/
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

/*    private Booking readBooking(ResultSet rs) {
        Booking booking = new Booking();
        try {
            booking.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDate(rs.getObject("DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getDate(\"DATE\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setHourStart(rs.getTime("HOUR_START").toInstant());
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"HOUR_START\", Instant.class)");
            throw new RuntimeException(e);
        }
        try {
            booking.setPrice(rs.getBigDecimal("PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBigDecimal(\"PRICE\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setHourEnd(rs.getTime("HOUR_END").toInstant());
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"HOUR_END\", Instant.class)");
            throw new RuntimeException(e);
        }
        try {
            booking.setIdEmployee(rs.getLong("ID_EMPLOYEE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_SUPPLIER\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setIdCustomer(rs.getLong("ID_CUSTOMER"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_CUSTOMER\")");
            throw new RuntimeException(e);
        }
        return booking;
    }*/

}
