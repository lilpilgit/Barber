package model.dao;

import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

public abstract class DAOFactory {
    /*
     * si tratta di una classe che produce i DAO in base a quello richiesto, un ProductDAO, UserDao, etc...
     * La sua implementazione (in quanto classe astratta) per MySql lo si trova in mySQLJDBCImpl.
     * Importerà tutti i possibili figli */

    public static String MYSQLJDBCIMPL = "MySQLJDBCImpl";

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

    public abstract void closeTransaction();

    public abstract ProductDAO getProductDAO();

    public abstract BookingDAO getBookingDAO();

    public abstract EmployeeDAO getEmployeeDAO();

    public abstract CustomerDAO getCustomerDAO();

    public abstract StructureDAO getStructureDAO();

    public abstract UserDAO getUserDAO();

    public abstract AdminDAO getAdminDAO();

    public static DAOFactory getDAOFactory(String whichFactory) {
        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new MySQLJDBCDAOFactory();
        } else
            return null;
    }
}
