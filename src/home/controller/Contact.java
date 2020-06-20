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

    public static void showContactForm(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call contact.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO userDAO = null;
        User loggedUser = null;
        User customer = null;
        boolean cookieValid = true;

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
            /* Acquisisco un DAOFactory per poter lavorare sul DB*/
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(userDAO.findById(loggedUser.getId()))) {
                    /* utente non autorizzato, invalido il cookie */
                    System.out.println("UTENTE NON AUTORIZZATO !");
                    home.controller.Home.logout(request, response);
                    cookieValid = false;
                }
            } else {
                /*La pagina Contact è pubblica anche per gli utenti non loggati */
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la business logic */
                if (loggedUser != null) {
                    customer = userDAO.findById(loggedUser.getId());
                }
            }


            /* Commit sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        if (cookieValid) {
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/contact");
            /* 4) Setto l'utente da cui estrapolare i dati necessari */
            if (loggedUser != null) {
                request.setAttribute("customer", customer);
            }
        }
    }

    public static void contact(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Process the request, FAKE: send email, and inform user of result.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO userDAO = null;
        User loggedUser = null;
        User customer = null;
        String name;
        String email;
        String text;
        String applicationMessage = "It was not possible to send email. Try later!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean contacted = false; /* risultato dell'operazione di invio di una mail */
        boolean cookieValid = true;

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

            userDAO = daoFactory.getUserDAO();

            customer = userDAO.findById(loggedUser.getId());

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(customer)) {
                    /* utente non autorizzato, invalido il cookie */
                    System.out.println("UTENTE NON AUTORIZZATO !");
                    home.controller.Home.logout(request, response);
                    cookieValid = false;
                }
            } else {
                /*La pagina Contact è pubblica anche per gli utenti non loggati */
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la logica di business */

                /* Fetching dei parametri provenienti dal form di contatto */
                name = request.getParameter("contact_name");/*required*/
                email = request.getParameter("contact_email");/*required*/
                text = request.getParameter("contact_message");/*required*/

                contacted = true; /* invio mail sempre a buon fine */
                System.out.println("Invio dell'email da parte di " + name + "----" + email + "----" + text);
                applicationMessage = "Message sent correctly. We will contact you as soon as possible.";

            }

            /* Commit sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");

            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        if (cookieValid) {
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
            /* 6) Oggetto contenente le informazioni dell'utente loggato */
            if(loggedUser != null){
                request.setAttribute("customer",customer);
            }
        }
    }


}
