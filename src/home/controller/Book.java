package home.controller;

import model.dao.BookingDAO;
import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Booking;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.time.LocalDate;
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

    public static void bookAppointment(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a BookingDAO to be able to enter the new appointment in Database.
         */

        LocalDate date = null;
        Time time = null;

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        User user = null;
        BookingDAO bookingDAO = null;
        Booking bookInserted = null; /* serve per conoscere l'id del prodotto appena aggiunto */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento */
        Structure structure = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;

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

            /* Fetching dei parametri provenienti dal form di inserimento e salvataggio nelle variabili locali */
            date = LocalDate.parse(request.getParameter("selected_date"));
            time = Time.valueOf(request.getParameter("selected_time"));
            System.err.println("VALORE DI TIME: " + time + " VALORE DI DATE: " + date);

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            user = userDAO.findById(loggedUser.getId());

            bookingDAO = daoFactory.getBookingDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo insert() su employeeDAO ) */
            structure = structureDAO.fetchStructure();

            /* Effettuo l'inserimento del nuovo dipendente */
            try {
                bookingDAO.insert(date, time, user, structure);
                inserted = true; /* Se non viene sollevata l'eccezione, l'impiegato è stato inserito correttamente*/
            } catch (DuplicatedObjectException e) {
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            if (inserted) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che e' stato inserito correttamente .*/
                applicationMessage = "Booking inserted SUCCESSFULLY.";
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione la prenotazione non è stato inserita.*/
                applicationMessage = "Booking insertion ERROR.";

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
        /* Setto gli attributi della request che verranno processati dalla book.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire un nuovo inserimento */
        request.setAttribute("viewUrl", "customer/book");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (inserted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
    }
}
