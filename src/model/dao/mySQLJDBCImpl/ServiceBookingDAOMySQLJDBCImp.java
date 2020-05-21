package model.dao.mySQLJDBCImpl;

import model.mo.ServiceBooking;
import model.dao.ServiceBookingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServiceBookingDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public ServiceBookingDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }
}
