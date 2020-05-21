package model.dao.mySQLJDBCImpl;

import model.dao.AdminDAO;
import model.mo.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public AdminDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }


}
