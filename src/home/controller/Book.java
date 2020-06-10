package home.controller;

import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Book {

    private Book() {}

    public static void showBook(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call book.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento */
        Structure structure = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            structureDAO = daoFactory.getStructureDAO();
            structure = structureDAO.fetchStructure();

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


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


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Attributo che indica quale struttura e' selezionata (Nel nostro caso solo una) */
        request.setAttribute("structure", structure);
        /* 4) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/book");
    }

    public static void bookAppointment(HttpServletRequest request, HttpServletResponse response){
        //TODO da implementare
    }
}
