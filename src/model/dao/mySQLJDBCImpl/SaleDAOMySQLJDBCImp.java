package model.dao.mySQLJDBCImpl;

import model.mo.Sale;
import model.dao.SaleDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SaleDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public SaleDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }
}
