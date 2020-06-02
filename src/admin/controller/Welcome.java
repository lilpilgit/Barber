package admin.controller;

import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Welcome {

    private Welcome() {
    }

    public static void welcome(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set data to rappresent on welcome page then call the admin/view.jsp
         * */
        DAOFactory sessionDAOFactory = null; //per i cookie
        String applicationMessage = null;
        UserDAO sessionUserDAO = null;
        User loggedUser = null;
        UserDAO userDAO = null;
        User user = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            System.err.println("loggedOn==> " + loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
//            /* 3) Application messagge da mostrare all'utente */
//            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "admin/view");
        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {

            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }
    }
}
