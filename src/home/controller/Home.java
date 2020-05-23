package home.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.UserDAO;
import model.mo.Product;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Home {

    private Home() {
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call the common/home.jsp
         * */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/home");
        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }

    }

    public static void logon(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check validity of creds on DB and set cookie. After that call common/home.jsp
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser;
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

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            /* check delle credenziali sul database */
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByEmail(email); /* tale utente esiste???? */

            /* se l'utente con tale email non esiste oppure ha inserito una password sbagliata */
            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.delete(null);
                applicationMessage = "Username e/o password errati!";
                loggedUser = null;
            } else {
                loggedUser = sessionUserDAO.insert(user.getId(), null, user.getName(), user.getSurname(), null, null, null, user.isAdmin(), user.isEmployee(), user.isCustomer());
            }


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/home");
        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {

            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        /**
         * Invalidate with max-age session cookie.
         */
        DAOFactory sessionDAOFactory = null;//per i cookie
        DAOFactory daoFactory = null; //per il db

        try {

            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/
            sessionUserDAO.delete(null); /* new Cookie("loggedUser", ""); +  cookie.setMaxAge(0);*/

            sessionDAOFactory.commitTransaction(); /* Commit fittizio */

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();


            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "common/home");

        } catch (Exception e) {
            try {
                /* Rollback fittizio */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                /* Close fittizia */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }


    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {

        ArrayList<Product> showcase = null;
        ProductDAO productDAO = daoFactory.getProductDAO();


        /* Scarico dal DB la lista dei prodotti da mostrare in vetrina */
        showcase = productDAO.findShowcaseProduct();

        /* Setto i prodotti della vetrina come parametro della request */
        request.setAttribute("showcase", showcase);


    }
}
