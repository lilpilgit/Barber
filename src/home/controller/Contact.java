package home.controller;

import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Contact {

    private Contact() {
    }

    public static void contact(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Process the request, FAKE: send email, and inform user of result.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        String name;
        String email;
        String text;
        String applicationMessage = "It was not possible to send email. Try later!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean contacted = false; /* risultato dell'operazione di invio di una mail */

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

            /* Fetching dei parametri provenienti dal form di contatto */
            name = request.getParameter("contact_name");/*required*/
            email = request.getParameter("contact_email");/*required*/
            text = request.getParameter("contact_message");/*required*/

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* TODO: operazioni di invio email*/
            contacted = true; /* invio mail sempre a buon fine */
            System.out.println("Invio dell'email da parte di " + name + "----" + email + "----" + text);
            applicationMessage = "Message sent correctly. We will contact you as soon as possible.";
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        /* Setto gli attributi della request che verranno processati dalla contact.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la registrazione */
        request.setAttribute("viewUrl", "common/contact");
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 5) oggetto corrispondente al risultato dell'operazione */
        request.setAttribute("contacted", contacted);
    }


    public static void showContactForm(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call contact.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "common/contact");
    }

}
