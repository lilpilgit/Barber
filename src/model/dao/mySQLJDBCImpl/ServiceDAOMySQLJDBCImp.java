package model.dao.mySQLJDBCImpl;

import model.mo.Service;
import model.dao.ServiceDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServiceDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public ServiceDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }
}
