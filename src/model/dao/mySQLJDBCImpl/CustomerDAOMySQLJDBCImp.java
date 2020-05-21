package model.dao.mySQLJDBCImpl;

import model.mo.Customer;
import model.dao.CustomerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public CustomerDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }

}
