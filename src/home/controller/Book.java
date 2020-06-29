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
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
            sessionDAOFactory = initializeCookie(request, response);

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
        Structure structure = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (inserted) {
                /* Solo se viene committata la transazione senza errori siamo sicuri l'appuntamento e' stato inserito correttamente .*/
                applicationMessage = "You successfully created your booking!";
            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione la prenotazione non è stato inserita.*/
                applicationMessage = "Booking creation ERROR.";

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

    public static void getBooking(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch the last booked appointment of a specific customer.
         *
         * WARNING! This method is compatible only with AJAX request.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        Structure structure = null;
        StructureDAO structureDAO = null;
        User loggedUser = null;
        BookingDAO bookingDAO = null;
        Booking booking = null;

        String result = "fail"; /* Se tutto va a buon fine, poi diventera' success */

        boolean alreadyBooked = false;
        boolean deletedByAdmin = false;

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

            bookingDAO = daoFactory.getBookingDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Faccio il fetch dell'unica struttura che ho nel db */
            structure = structureDAO.fetchStructure();

            /* Creo l'oggetto booking che conterra' tutte le informazioni riferite allo status dell'ultimo appuntamento */
            booking = bookingDAO.getLastBooking(loggedUser.getId(), structure.getId());

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione il prodotto non è stato modificato .*/
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");

            }
        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }


    /* Nel caso in cui la bookingDAO.getLastBooking(idCustomer, structure.getId()); ritorni un oggetto diverso da null
       significa che c'e' una prenotazione nel db riferita a quell'utente.
       A questo punto se il valore di DELETED nel db e' null, significa che c'e' una prenotazione che non e' stata
       cancellata ne' dall'amministratore, ne' dal cliente. Quindi setto alreadyBooked a true per impedire al cliente
       di effettuare una nuova prenotazione.
     */

        if (booking != null) {
            if (booking.isDeleted() == null)
                alreadyBooked = true;
            else if (!booking.isDeleted())
                deletedByAdmin = true;
        }

        result = "success";

        response.setContentType("application/json");
        /* Scrivo il json sulla response */
        try {
            response.getWriter().write("{");
            response.getWriter().append("\"result\":\"").append(result).append("\"");
            response.getWriter().append(",");
            response.getWriter().append("\"alreadyBooked\":\"").append(String.valueOf(alreadyBooked)).append("\"");
            if (alreadyBooked || deletedByAdmin) {
                response.getWriter().append(",");
                response.getWriter().append("\"idBooking\":\"").append(String.valueOf(booking.getId())).append("\"");
                response.getWriter().append(",");
                response.getWriter().append("\"deleted\":\"").append(String.valueOf(booking.isDeleted())).append("\"");
                response.getWriter().append(",");
                response.getWriter().append("\"deletedReason\":\"").append(booking.getDeletedReason()).append("\"");
                response.getWriter().append(",");
                response.getWriter().append("\"date\":\"").append(String.valueOf(booking.getDate())).append("\"");
                response.getWriter().append(",");
                response.getWriter().append("\"hourStart\":\"").append(String.valueOf(booking.getHourStart())).append("\"");
            }
            response.getWriter().append("}");
        } catch (IOException e) {
            System.out.println("Errore nella response.getWriter().write()");
            e.printStackTrace();
        }
        try {
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void reservedSlot(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch arraylist of booking time for a specific date.
         *
         * WARNING! This method is compatible only with AJAX request.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        Structure structure = null;
        StructureDAO structureDAO = null;
        BookingDAO bookingDAO = null;
        ArrayList<Booking> bookings = null;
        String result = "fail"; /* Se tutto va a buon fine, poi diventera' success */
        ArrayList<LocalTime> freeSlots = null;
        int i = 0;

        LocalTime openingTime = null; /* parametro che rappresenta l'ora di apertura della struttura */
        LocalTime closingTime = null; /* parametro che rappresenta l'ora di chiusura della struttura */
        LocalTime currentTime = null; /* parametro che rappresenta l'ora attuale */
        LocalTime slot = null; /* parametro che serve per fare lo scanning degli appuntamenti in una giornata */
        LocalDate pickedDate = null; /* parametro che rappresenta l'ora selezionata dall'utente */
        LocalDate currentDate = null; /* parametro che rappresenta la data attuale */

        boolean isToday = false;

        try {

            /* Acquisisco un DAOFactory per poter lavorare sul DB*/
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            daoFactory.beginTransaction();

            bookingDAO = daoFactory.getBookingDAO();
            structureDAO = daoFactory.getStructureDAO();

            structure = structureDAO.fetchStructure();
            openingTime = structure.getOpeningTime().toLocalTime();
            closingTime = structure.getClosingTime().toLocalTime();
            /* setto l'ora attuale ricevuta dal client */
            currentTime = LocalTime.parse(request.getParameter("currentTime"));

            slot = structure.getSlot().toLocalTime();

            /* setto la data selezionata dall'utente */
            pickedDate = LocalDate.parse(request.getParameter("pickedDate"));

            /* setto la data attuale fornita dal client */
            currentDate = LocalDate.parse(request.getParameter("currentDate"));

            /* importo l'array di prenotazioni riferite alla data selezionata dall'utente */
            bookings = bookingDAO.findBookingsByDate(pickedDate);

            /* Commit sul db */
            daoFactory.commitTransaction();

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/

                /* Se viene fatto il rollback della transazione il prodotto non è stato modificato .*/
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");

            }
        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        LocalTime indexTime = null; /* Uso un indice per scandire tutti gli intervalli temporanei definiti dallo slot */
        /* verifico se la data di prenotazione e' uguale alla data attuale del client */
        isToday = pickedDate.isEqual(currentDate);

        freeSlots = new ArrayList<LocalTime>();


        for (indexTime = LocalTime.of(openingTime.getHour(), openingTime.getMinute(), openingTime.getSecond());
             !indexTime.equals(closingTime); indexTime = indexTime.plusMinutes(slot.getMinute())) {

            if (i < bookings.size() && indexTime.equals(bookings.get(i).getHourStart().toLocalTime())) {
                System.err.println("Trovato appuntamento alle: " + bookings.get(i).getHourStart());
                i++;
            } else {
                /* se non si sta prenotando per la data odierna, salvo tutti gli slot liberi */
                if (!isToday || (isToday && (indexTime.compareTo(currentTime) > 0))) {
                    freeSlots.add(indexTime);
                    /* altrimenti se si sta prenotando per la data odierna, inserisco in freeSlots solo gli orari successivi a quello attuale */
                }
            }
        }

        result = "success";

        response.setContentType("application/json");
        /* Scrivo il json sulla response */
        try {
            response.getWriter().write("{");
            response.getWriter().append("\"result\":\"").append(result).append("\"");
            response.getWriter().append(",");
            response.getWriter().append("\"availableTimes\":");
            response.getWriter().append("[");
            for (i = 0; i < freeSlots.size(); i++) {
                response.getWriter().append("\"").append(String.valueOf(freeSlots.get(i))).append("\"");
                if (i != (freeSlots.size() - 1)) {
                    response.getWriter().append(",");
                } else {
                    response.getWriter().append("]");
                }
            }
            response.getWriter().append("}");
        } catch (IOException e) {
            System.out.println("Errore nella response.getWriter().write()");
            e.printStackTrace();
        }
        try {
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
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
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (deleted) {
                applicationMessage = "Booking deleted SUCCESSFULLY. We are sorry that you had to cancel your appointment.";
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

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

        /* 3) Attributo che indica quale struttura e' selezionata (Nel nostro caso solo una) */
        request.setAttribute("structure", structure);

        /* 4) Attributo che indica se un cliente ha gia' effettuato un appuntamento futuro */
        request.setAttribute("bookedStatus", bookedStatus);

        /* 5) Setto l'oggetto struttura da mostrare in ogni footer dell'area customer */
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
