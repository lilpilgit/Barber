package admin.controller;

import model.dao.BookingDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.Booking;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static void commonView(DAOFactory daoFactory,LocalDate currentDate,HttpServletRequest request){
        BookingDAO bookingDAO = daoFactory.getBookingDAO();
        ArrayList<Booking> bookings = bookingDAO.findBookingsByDateForAdmin(currentDate);

        /* La lista di prenotazioni da mostrare*/
        request.setAttribute("bookings", bookings);

        /* Data da mostrare di volta in volta nel calendario della navbar */
        request.setAttribute("currentDate",currentDate);

    }

}


