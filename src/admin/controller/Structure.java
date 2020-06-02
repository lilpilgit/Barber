package admin.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;


public class Structure {

    private Structure() {

    }

    public static void showStructure(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an StructuresDAO to be able to show Structure in Database.
         */
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

            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
            }
        }

        /* Setto gli attributi della request che verranno processati dalla edit-structure.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 1) Attributo che indica la pagina da visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/edit-structure");

    }

    public static void editStructure(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an StructureDAO to be able to edit the existing structure in Database.
         */

//        String submit; /*mi aspetto che il value sia "edit_structure"*/
        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        model.mo.Structure structureToEdit = null;
        model.mo.Structure originalStructure = null; /* l'oggetto intatto ancora con i campi non modificati */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;

//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_structure"*/

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

            /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
             * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

            /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
             * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
             * */
            structureToEdit.setName(request.getParameter("name"));
            structureToEdit.setPhone(request.getParameter("phone"));
            structureToEdit.setOpeningTime(Instant.parse(request.getParameter("opening_time")));
            structureToEdit.setClosingTime(Instant.parse(request.getParameter("closing_time")));
            structureToEdit.setSlot(Instant.parse(request.getParameter("slot")));
            structureToEdit.setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number")));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            structureDAO = daoFactory.getStructureDAO();

            originalStructure = structureDAO.fetchStructure();

            /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
             * quale passare alla pagina edit-structure.jsp */
            structureToEdit = structureDAO.fetchStructure();


            /* Effettuo la modifica della struttura */
            edited = structureDAO.update(structureToEdit);/* Se non viene sollevata l'eccezione, la struttura è stato modificata correttamente*/

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            if (edited) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che la struttura sia stata modificata correttamente .*/
                applicationMessage = "Structure modified SUCCESSFULLY.";
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione la struttura non è stata modificata .*/
                applicationMessage = "Structure modification ERROR.";
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
            }
        }

        /* Setto gli attributi della request che verranno processati dalla edit-structure.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di modifica solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo admin */
        request.setAttribute("viewUrl", "admin/edit-structure");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 6) la struttura che è stata modificata e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("structureToModify", structureToEdit);
        } else {
            /* FAIL */
            request.setAttribute("structureToModify", originalStructure);
        }

    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        StructureDAO structureDAO = null;
        model.mo.Structure structure = null;

        structureDAO = daoFactory.getStructureDAO();

        /* Scarico dal DB la struttura */
        structure = structureDAO.fetchStructure();

        request.setAttribute("structureToModify", structure);
    }
}

