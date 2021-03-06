package home.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Profile {
    private Profile() {
    }

    public static void showProfile(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call profile.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        User user = null;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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

            user = userDAO.findById(loggedUser.getId());

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(user)) {
                    /* utente non autorizzato, invalido il cookie */
                    System.out.println("UTENTE NON AUTORIZZATO !");
                    home.controller.Home.logout(request, response);
                    cookieValid = false;
                }
            } else {
                System.out.println("ACCESSO AD AREA RISERVATA DA PARTE DI UN UTENTE NON LOGGATO. ACCESSO VIETATO. ");
                cookieValid = false;
                request.setAttribute("viewUrl", "error/404");
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Chiamo la commonView */
                commonView(daoFactory, request);
            }

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

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
            request.setAttribute("viewUrl", "customer/profile");
            /* 4) Setto il cliente da mostrare in base a quale utente è loggato */
            request.setAttribute("customer", user);
        }
    }

    public static void updateProfile(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a UserDAO to be able to edit the existing user in Database.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        User userToUpdate = null;
        User originalUser = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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
                System.out.println("ACCESSO AD AREA RISERVATA DA PARTE DI UN UTENTE NON LOGGATO. ACCESSO VIETATO. ");
                cookieValid = false;
                request.setAttribute("viewUrl", "error/404");
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la logica di business */
                originalUser = userDAO.findById(loggedUser.getId());

                /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
                 * quale passare alla pagina profile.jsp */

                userToUpdate = userDAO.findById(loggedUser.getId());

                /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
                 * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

                userToUpdate.setEmail(request.getParameter("email"));
                userToUpdate.setName(request.getParameter("name"));
                userToUpdate.setSurname(request.getParameter("surname"));
                userToUpdate.setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number")));
                userToUpdate.setPhone(request.getParameter("phone"));
                userToUpdate.setType('C');
                userToUpdate.setBlocked(false);

                /* Effettuo la modifica del cliente */
                try {
                    edited = userDAO.update(userToUpdate);/* Se non viene sollevata l'eccezione, l'utente è stato modificato correttamente*/

                } catch (DuplicatedObjectException e) {
                    applicationMessage = "Error: Your data could not be updated. The email entered already exists.";
                    e.printStackTrace();
                }

                commonView(daoFactory, request);

            }

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (edited) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che l'utente sia stato modificato correttamente .*/
                applicationMessage = "Your data has been modified SUCCESSFULLY.";
            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione il cliente non è stato modificato .*/
                applicationMessage = "Error: Your data could not be updated.";
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
            throw new RuntimeException(e);

        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        if (cookieValid) {
            /* Setto gli attributi della request che verranno processati dalla profile.jsp */

            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
             *     form per consentire ulteriori modifiche sul medesimo impiegato */
            request.setAttribute("viewUrl", "customer/profile");
            /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (edited) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
            /* 4) il cliente che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
            if (edited) {
                /* SUCCESS */
                request.setAttribute("customer", userToUpdate);
            } else {
                /* FAIL */
                request.setAttribute("customer", originalUser);
            }
        }
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {

        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = null;

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

        /* Setto l'oggetto struttura da mostrare in ogni footer dell'area customer */
        request.setAttribute("structure", structure);

    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }

}
