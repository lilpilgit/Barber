<%@ page import="model.dao.BookingDAO"%>
<%@ page import="model.dao.DAOFactory"%>
<%@ page import="model.dao.StructureDAO"%>
<%@ page import="model.dao.UserDAO"%>
<%@ page import="model.mo.Booking"%>
<%@ page import="model.mo.Structure"%>
<%@ page import="model.mo.User"%>
<%@ page import="services.config.Configuration"%>
<%@ page import="java.sql.Time"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.util.ArrayList"%><%@ page import="java.util.HashMap"%>
<%@ page contentType="text/plain" pageEncoding="UTF-8"%>

<%
    /**
     * Fetch arraylist of booking time for a specific date.
     *
     * WARNING! This method is compatible only with AJAX request.
     */

    DAOFactory sessionDAOFactory = null; //per i cookie
    DAOFactory daoFactory = null; //per il db
    User loggedUser = null;
    UserDAO userDAO = null;
    Structure structure = null;
    StructureDAO structureDAO = null;
    BookingDAO bookingDAO = null;
    ArrayList<Booking> bookings = null;
    ArrayList<String> availableTimes = null;
    String result = "fail"; /* Se tutto va a buon fine, poi diventera' success */

    User user = null;
    Long idStructure = null; /* parametro che indica la struttura in cui si sta eseguendo la prenotazione */
    LocalTime openingTime = null; /* parametro che rappresenta l'ora di apertura della struttura */
    LocalTime closingTime = null; /* parametro che rappresenta l'ora di chiusura della struttura */
    LocalTime slot = null; /* parametro che serve per fare lo scanning degli appuntamenti in una giornata */
    LocalDate pickedDate = null; /* parametro che rappresenta l'ora selezionata dall'utente */

    boolean changed = false;

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

        System.err.println("FINO A QUI TUTTO BENE...");

        daoFactory.beginTransaction();

        userDAO = daoFactory.getUserDAO();

        user = userDAO.findById(loggedUser.getId());

        bookingDAO = daoFactory.getBookingDAO();
        structureDAO = daoFactory.getStructureDAO();

        /* setto l'id della struttura sulla base dell'id ricevuto */
        idStructure = Long.valueOf(request.getParameter("idStructure"));

        structure = structureDAO.fetchStructure();
        openingTime = structure.getOpeningTime().toLocalTime();
        System.err.println("Valore di openingTime: " + openingTime);
        closingTime = structure.getClosingTime().toLocalTime();
        System.err.println("Valore di closingTime: " + closingTime);
        slot = structure.getSlot().toLocalTime();
        System.err.println("Valore di slot: " + slot);

        /* setto la data selezionata dall'utente */
        pickedDate = LocalDate.parse(request.getParameter("pickedDate"));
        System.err.println("Valore di pickedDate: " + pickedDate );

        /* importo l'array di prenotazioni riferite alla data selezionata dall'utente */
        bookings = bookingDAO.findBookingsByDate(pickedDate);

        System.err.println("toString di bookings:" + bookings.toString());



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

    LocalTime indexTime = null; /* Uso un indice per scandire tutti gli intervalli temporanei definiti dallo slot */
    ArrayList<LocalTime> freeSlots = null;  /* salvo gli slot vuoti in un arraylist */
    int i = 0;

                System.err.println("Inizio ciclo for");

                for(Booking b : bookings)
                    System.err.println("Trovata: " + b.getHourStart());

//    for (indexTime.adjustInto(openingTime); indexTime.equals(closingTime.minusMinutes(slot.getMinute())); indexTime = indexTime.plusMinutes(slot.getMinute())) {
//
//                        System.err.println("Dentro ciclo for");
//        if(indexTime.equals(bookings.get(i).getHourStart().toLocalTime())) {
//            i++;
//            } else {
//            freeSlots.add(indexTime);
//            System.err.println("Aggiunto nell'array: " + indexTime);
//        }
//    }

    result = "success";
%>

            { "result": "success",

            "availableTimes":

            [ time1, time2,


            ]

            }