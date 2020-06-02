package admin.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;

public class Admin {

    private Admin() {
    }

    public static void showProfile(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch info about logged admin and call edit-admin.jsp
         * */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO userDAO = null;
        User adminToEdit = null; /* oggetto di classe User che deve essere passato alla pagina del form di modifica */
        User loggedUser;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            loggedUser = sessionUserDAO.findLoggedUser(); /* trovo l'oggetto user dell'admin attualmente loggato */

            /* Acquisisco un DAOFactory per poter lavorare sul DB*/
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            adminToEdit = userDAO.findByEmail(loggedUser.getEmail());

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT DELLA TRANSAZIONE");
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
                System.err.println("ERRORE NEL ROLLBACK DELLA TRANSAZIONE");
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

        /* Setto gli attributi della request */
        /* 1) Booleano per sapere se è loggato o meno */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Oggetto che specifica quale utente è loggato*/
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il viewUrl che il dispatcher dovrà visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/edit-admin"); /* bisogna visualizzare edit-admin.jsp */
        /* 4) l'oggetto impiegato che deve essere modificato */
        request.setAttribute("adminToModify", adminToEdit);


    }

    public static void editAdmin(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a UserDAO to be able to edit the existing admin in Database.
         */

//        String submit; /*mi aspetto che il value sia "edit_admin"*/

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        User adminToEdit = null;
        User originalAdmin = null; /* l'oggetto intatto ancora con i campi non modificati */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;


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

            originalAdmin = userDAO.findByEmail(loggedUser.getEmail());

            /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
             * quale passare alla pagina profile.jsp */

            adminToEdit = userDAO.findByEmail(loggedUser.getEmail());


            /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
             * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

            adminToEdit.setEmail(request.getParameter("email"));
            adminToEdit.setName(request.getParameter("name"));
            adminToEdit.setSurname(request.getParameter("surname"));
            adminToEdit.setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number")));
            adminToEdit.setPhone(request.getParameter("phone"));
            /* TODO:cambio password per l'admin */
            adminToEdit.setBirthDate(LocalDate.parse(request.getParameter("birth_date")));
            adminToEdit.setFiscalCode(request.getParameter("fiscal_code"));
            adminToEdit.setType('A');
            adminToEdit.setBlocked(false);
            /* TODO:cancellazione account dell'admin */

            /* Effettuo la modifica dell'admin */
            try {
                edited = userDAO.update(adminToEdit);/* Se non viene sollevata l'eccezione, l'admin è stato modificato correttamente*/

            } catch (DuplicatedObjectException e) {
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (edited) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che l'admin sia stato modificato correttamente .*/
                applicationMessage = "Your data has been modified SUCCESSFULLY.";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

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

        /* Setto gli attributi della request che verranno processati dalla edit-admin.jsp */

        /* 1) il messaggio da visualizzare nella pagina di modifica solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo admin */
        request.setAttribute("viewUrl", "admin/edit-admin");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) l'admin che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("adminToModify", adminToEdit);
        } else {
            /* FAIL */
            request.setAttribute("adminToModify", originalAdmin);
        }

    }
}
