package admin.controller;

import model.dao.BookingDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.Booking;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Bookings {


    public static void showBookings(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a DAOFactory to be able to show ALL employees in Database with call to commonView.
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        LocalDate currentDate = null;
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

            currentDate = LocalDate.parse(request.getParameter("currentDate"));

            System.out.println("OOOOOOOOOOOO "+ currentDate);

            commonView(daoFactory,currentDate,request); /* setto l'attributo "bookings" */

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

        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Attributo riguardante la viewUrl da visualizzare */
        request.setAttribute("viewUrl", "admin/show-bookings");
    }

    public static void deleteBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates a BookingDAO to be able to delete delete booking from Database and set the deletedReason.
         */
        Long idToDelete = null; /* id del booking ricevuto da cancellare*/
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        DAOFactory daoFactory = null;
        BookingDAO bookingDAO = null; /* DAO Necessario per poter effettuare la cancellazione del booking */
        Booking bookingToDelete = null;
        String deletedReason = null;
        LocalDate currentDate = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;


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

            /* Fetching dell'id dell'appuntamento da cancellare proveniente dal form dentro la pagina show-bookings.jsp */
            idToDelete = Long.valueOf(request.getParameter("bookingID"));
            /* Fetching della deleted reason */
            deletedReason = request.getParameter("deletedReason");
            /* Fetching della data da mostrare */
            currentDate = LocalDate.parse(request.getParameter("currentDate"));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* è necessario prendersi solo il bookingDAO per poter settare il flag DELETED e la DELETED_REASON */
            bookingDAO = daoFactory.getBookingDAO();

            /* trovo la prenotazione da flaggare come cancellata */
            bookingToDelete = bookingDAO.findBookingById(idToDelete);

            /* aggiungo i campi che la rendono cancellata dall'admin */
            bookingToDelete.setDeletedReason(deletedReason);

            bookingToDelete.setDeleted(false); /* 0 quando viene modificato dall'admin */

            deleted = bookingDAO.deleteBooking(bookingToDelete); /* Se non viene sollevata l'eccezione, l'appuntamento è stato cancellato correttamente*/

            commonView(daoFactory,currentDate, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */


            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            if (deleted) {
                /* Se l'impiegato è stato  cancellato committo la transazione */
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato cancellato correttamente .*/
                applicationMessage = "Booking deleted SUCCESSFULLY.";
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato cancellato .*/
                applicationMessage = "Booking cancellation ERROR.";

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

        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-bookings.jsp per consentire altre cancellazioni */
        request.setAttribute("viewUrl", "admin/show-bookings");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (deleted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
    }

    private static void commonView(DAOFactory daoFactory,LocalDate currentDate,HttpServletRequest request){
        BookingDAO bookingDAO = daoFactory.getBookingDAO();
        ArrayList<Booking> bookings = bookingDAO.findBookingsByDateForAdmin(currentDate);

        /* La lista di prenotazioni da mostrare*/
        request.setAttribute("bookings", bookings);

        /* Data da mostrare di volta in volta nel calendario della navbar */
        request.setAttribute("currentDate",currentDate);

    }

}


