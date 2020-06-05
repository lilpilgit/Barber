package home.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Product {

    private Product(){}

    public static void showProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call product.jsp with id of product to show.
         */

        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;

        String applicationMessage = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/

            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Fetching dei parametri  */
            String idProduct = request.getParameter("idProduct");
            Long id = 1L;
            if (idProduct != null) {
                /* posso provare a parsarlo per evitare NullPointerException*/
                id = Long.parseLong(idProduct);
            }
            System.err.println("id:" + id);

            commonView(daoFactory,loggedUser,id,request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            boolean loggedOn = loggedUser != null;
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedOn);
            System.err.println("loggedOn==>" + loggedOn);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/product");

        } catch (
                Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

            } catch (Throwable t) {
            }
        }
    }

    public static void commonView(DAOFactory daoFactory,User loggedUser,Long id, HttpServletRequest request){

        ProductDAO productDAO = null; /* per fetchare il prodotto */
        UserDAO userDAO = null;
        model.mo.Product product = null; /* prodotto fetchato dal db da mostrare nella pagina product.jsp */
        boolean inWishlist = false; /* flag per sapere se il prodotto è nella wishlist dell'utente loggato */

        productDAO = daoFactory.getProductDAO();

        product = productDAO.findProductById(id);

        userDAO = daoFactory.getUserDAO();

        inWishlist = userDAO.inWishlist(loggedUser,id);

        System.err.println("product:" + product);
        System.err.println("inWishlist:" + inWishlist);
        /* 5) Setto il prodotto da mostrare */
        request.setAttribute("product", product);
        /* 6) Setto il flag per sapere se il prodotto è in wishlist già per l'utente loggato */
        request.setAttribute("inWishlist",inWishlist);
    }
}
