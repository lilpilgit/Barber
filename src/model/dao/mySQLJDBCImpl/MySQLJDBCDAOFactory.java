package model.dao.mySQLJDBCImpl;

import model.dao.*;
import services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Connection connection = null;
    private HashMap factoryParameters = null;

    public MySQLJDBCDAOFactory(HashMap factoryParameters) {
        this.factoryParameters = factoryParameters;
    }

    @Override
    public void beginTransaction() {
        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false); /*     IMPORTANT !!!!!! */
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            System.err.println("Errore nella commitTransaction().");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            System.err.println("Errore nella rollbackTransaction().");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.err.println("Errore nella closeTransaction().");
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDAO getProductDAO() {
        return new ProductDAOMySQLJDBCImpl(connection);
    }

    @Override
    public BookingDAO getBookingDAO() {
        return new BookingDAOMySQLJDBCImpl(connection);
    }

    @Override
    public StructureDAO getStructureDAO() {
        return new StructureDAOMySQLJDBCImpl(connection);
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CartDAO getCartDAO() {
        return new CartDAOMySQLJDBCImpl(connection);
    }

    @Override
    public WishlistDAO getWishlistDAO() {
        return new WishlistDAOMySQLJDBCImpl(connection);
    }

}
