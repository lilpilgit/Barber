package model.dao;

import model.dao.CookieImpl.CookieDAOFactory;
import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {
    /*
     * si tratta di una classe che produce i DAO in base a quello richiesto, un ProductDAO, UserDao, etc...
     * La sua implementazione (in quanto classe astratta) per MySql lo si trova in mySQLJDBCImpl.
     * Importerà tutti i possibili figli.
     *
     * L'implementazione dei cookie invece è possibile trovarla nel package CookieImpl */

    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";

    public static final String COOKIEIMPL = "CookieImpl";

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

    public abstract void closeTransaction();

    public abstract ProductDAO getProductDAO();

    public abstract BookingDAO getBookingDAO();

    public abstract StructureDAO getStructureDAO();

    public abstract UserDAO getUserDAO();

    public abstract CartDAO getCartDAO();

    public abstract OrdersDAO getOrdersDAO();

    public abstract WishlistDAO getWishlistDAO();

    public abstract StatisticsDAO getStatisticsDAO();

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters) {
        /* ritorna oggetti che rappresentano le istanze delle classi concrete che consentono di accedere effettivamente al DB, ai cookie ...*/
        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new MySQLJDBCDAOFactory(factoryParameters);
        } else if (whichFactory.equals(COOKIEIMPL)) {
            return new CookieDAOFactory(factoryParameters);
        } else
            return null;
    }
}
