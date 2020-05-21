package model.dao.mySQLJDBCImpl;

import model.mo.Purchase;
import model.dao.PurchaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PurchaseDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public PurchaseDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }
}
