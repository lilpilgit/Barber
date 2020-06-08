package model.dao.CookieImpl;

import model.dao.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters = factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request = (HttpServletRequest) factoryParameters.get("request");
            this.response = (HttpServletResponse) factoryParameters.get("response");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /* Dato che estende DAOFactory è costretto a dover implementare tali metodi anche se in caso di cookie
     * commit,rollback,close non hanno alcun significato.*/
    @Override
    public void commitTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void closeTransaction() {
    }

    @Override
    public ProductDAO getProductDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BookingDAO getBookingDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StructureDAO getStructureDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOCookieImpl(request, response);
    }

    @Override
    public CartDAO getCartDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OrdersDAO getOrdersDAO()  {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WishlistDAO getWishlistDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}