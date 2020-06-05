package home.controller;

import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Contact {

    private Contact(){}

    public static void contact(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Process the request, FAKE: send email, and inform user of result.
         */
        String name;
        String email;
        String text;

        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare l'inserimento del cliente */

        String applicationMessage = "It was not possible to send email. Try later!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean contacted = false; /* risultato dell'operazione di invio di una mail */
        User user = null; /* utente che sta usando la form di contatto */
        boolean registered = false; /* per sapere se l'utente che contatta è già registrato o meno */

        /* Fetching dei parametri provenienti dal form di registrazione*/
        name = request.getParameter("contact_name");/*required*/
        email = request.getParameter("contact_email");/*required*/
        text = request.getParameter("contact_message");/*not required*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Home.register ==> daoFactory.beginTransaction();");
        }

        userDAO = daoFactory.getUserDAO();

        /* Verifico se è  un cliente già registrato o meno TODO:per ulteriori statistiche */

        user = userDAO.findByEmail(email);
        if (user != null) {
            /* utente già registrato */
            registered = true;
        }
        /* TODO: operazioni di invio email*/
        contacted = true; /* invio mail sempre a buon fine */
        System.out.println("Invio dell'email da parte di " + name + "----" + email + "----" + text);
        applicationMessage = "Message sent correctly. We will contact you as soon as possible.";


        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            daoFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla contact.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la registrazione */
        request.setAttribute("viewUrl", "common/contact");
        /* 3) il booleano per sapere se è loggato o meno ( dopo la registrazione non posso essere loggato ) */
        request.setAttribute("loggedOn", false);
        /* 4) oggetto corrispondente all'utente loggato ( dopo la registrazione non posso essere loggato dunque null ) */
        request.setAttribute("loggedUser", null);
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
