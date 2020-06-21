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

    private Book() {
    }

    public static void showBook(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call book.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO userDAO = null;
        User loggedUser = null;
        boolean cookieValid = true;


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
                /* Chiamo la commonView */
                commonView(daoFactory, loggedUser, request);
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


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
            /* 5) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "customer/book");
        }
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
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento */
        Booking bookInserted = null; /* serve per conoscere l'id del prodotto appena aggiunto */
        Structure structure = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;
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
                request.setAttribute("viewUrl", "error/404");            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la logica di business */

                user = userDAO.findById(loggedUser.getId());

                bookingDAO = daoFactory.getBookingDAO();

                /* Prendo l'unica struttura */

                structureDAO = daoFactory.getStructureDAO();

                structure = structureDAO.fetchStructure();

                /* Fetching dei parametri provenienti dal form di inserimento e salvataggio nelle variabili locali */
                date = LocalDate.parse(request.getParameter("selected_date"));
                time = Time.valueOf(request.getParameter("selected_time"));
                System.err.println("VALORE DI TIME: " + time + " VALORE DI DATE: " + date);

                System.err.println("VALORE DATE: " + date + " VALORE TIME " + time + " Valore USER " + user.getId() + " VALORE STRUCTURE " + structure.getId());

                /* Effettuo l'inserimento del nuovo appuntamento */
                try {
                    bookingDAO.insert(date, time, user, structure);
                    inserted = true; /* Se non viene sollevata l'eccezione, l'appuntamento è stato inserito correttamente*/
                } catch (DuplicatedObjectException e) {
                    applicationMessage = e.getMessage();
                    e.printStackTrace();
                }

                /* Setto l'attributo alreadyBooked */
                commonView(daoFactory, loggedUser, request);

                if (inserted) {
                    /* Solo se viene committata la transazione senza errori siamo sicuri che e' stato inserito correttamente .*/
                    applicationMessage = "Booking inserted SUCCESSFULLY.";
                }
            }

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

        if (cookieValid) {
            /* Setto gli attributi della request che verranno processati dalla book.jsp */
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Attributo che indica in quale struttura si trova loggato l'utente */
            request.setAttribute("structure", structure);
            /* 5) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
             *     form per consentire un nuovo inserimento */
            request.setAttribute("viewUrl", "customer/book");
            /* 6) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (inserted) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
        }
    }

    public static void deleteBooking(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a BookingDAO to be able to delete delete booking from Database and set the deletedReason.
         */
        Long idToDelete = null; /* id del booking ricevuto da cancellare*/
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        UserDAO userDAO = null;
        DAOFactory daoFactory = null;
        BookingDAO bookingDAO = null; /* DAO Necessario per poter effettuare la cancellazione del booking */
        Booking bookingToDelete = null;
        String deletedReason = null;
        LocalDate currentDate = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;
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

            /* Fetching dell'id dell'appuntamento da cancellare proveniente dal form dentro la pagina show-bookings.jsp */
            System.err.println("VALORE DI bookingID: " + request.getParameter("bookingID"));
            idToDelete = Long.valueOf(request.getParameter("bookingID"));
            /* Fetching della deleted reason */
            deletedReason = request.getParameter("deletedReason");

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
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
                request.setAttribute("viewUrl", "error/404");            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la logica di business */
                /* è necessario prendersi solo il bookingDAO per poter settare il flag DELETED e la DELETED_REASON */
                bookingDAO = daoFactory.getBookingDAO();

                /* trovo la prenotazione da flaggare come cancellata (uso findBookingById perche' mi interessano
                 le prenotazioni con stato null dato che quelle con 1 non le vedo, e quelle con 0 vengono gestite in sola lettura) */
                bookingToDelete = bookingDAO.findBookingById(idToDelete);

                /* aggiungo i campi che la rendono cancellata dall'utente */
                bookingToDelete.setDeletedReason(deletedReason);

                bookingToDelete.setDeleted(true); /* 1 quando viene modificato dal customer */

                deleted = bookingDAO.deleteBooking(bookingToDelete); /* Se non viene sollevata l'eccezione, l'appuntamento è stato cancellato correttamente*/

                /* Setto l'attributo alreadyBooked */
                commonView(daoFactory, loggedUser, request);

                if (deleted) {
                    applicationMessage = "Booking deleted SUCCESSFULLY. We are sorry that you had to cancel your appointment.";
                }

            }

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
        if (cookieValid) {

            /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) il messaggio da visualizzare nella pagina di elenco solo se non è null */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
             *     la show-bookings.jsp per consentire altre cancellazioni */
            request.setAttribute("viewUrl", "customer/book");
            /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (deleted) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
        }
    }

    public static void commonView(DAOFactory daoFactory, User loggedUser, HttpServletRequest request) {

        /**
         * Set attribute "structure" and "alreadyBooked" inside request
         */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento */
        Structure structure = null;
        BookingDAO bookingDAO = null;
        Booking booking = null;
        boolean bookedStatus = false;


        /* Faccio il fetch della struttura */
        structureDAO = daoFactory.getStructureDAO();
        structure = structureDAO.fetchStructure();

        /* Controllo se ci sta già un appuntamento prenotato */
        bookingDAO = daoFactory.getBookingDAO();

        booking = bookingDAO.getLastBooking(loggedUser.getId(), structure.getId());

        if (booking != null) {
            if (booking.isDeleted() == null)
                bookedStatus = true;
        }

        /* 3) Attributo che indica quale struttura e' selezionata (Nel nostro caso solo una) */
        request.setAttribute("structure", structure);

        /* 4) Attributo che indica se un cliente ha gia' effettuato un appuntamento futuro */
        request.setAttribute("bookedStatus", bookedStatus);

    }
}
