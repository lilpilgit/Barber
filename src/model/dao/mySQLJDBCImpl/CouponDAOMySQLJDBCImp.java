package model.dao.mySQLJDBCImpl;

import model.mo.Coupon;
import model.dao.CouponDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CouponDAOMySQLJDBCImp {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public CouponDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }


}
