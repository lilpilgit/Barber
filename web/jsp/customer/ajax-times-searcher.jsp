<%@ page import="model.dao.BookingDAO"%>
<%@ page import="model.dao.DAOFactory"%>
<%@ page import="model.dao.StructureDAO"%>
<%@ page import="model.dao.UserDAO"%>
<%@ page import="model.mo.Booking"%>
<%@ page import="model.mo.Structure"%>
<%@ page import="model.mo.User"%>
<%@ page import="services.config.Configuration"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page contentType="text/plain" pageEncoding="UTF-8"%>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>

<%
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
    User loggedUser = null;
    UserDAO userDAO = null;
    ArrayList<Booking> bookings = null;
    String result = "fail"; /* Se tutto va a buon fine, poi diventera' success */
    ArrayList<LocalTime> freeSlots = null;
    int i = 0;

    Long idStructure = null; /* parametro che indica la struttura in cui si sta eseguendo la prenotazione */
    LocalTime openingTime = null; /* parametro che rappresenta l'ora di apertura della struttura */
    LocalTime closingTime = null; /* parametro che rappresenta l'ora di chiusura della struttura */
    LocalTime currentTime = null; /* parametro che rappresenta l'ora attuale */
    LocalTime slot = null; /* parametro che serve per fare lo scanning degli appuntamenti in una giornata */
    LocalDate pickedDate = null; /* parametro che rappresenta l'ora selezionata dall'utente */
    LocalDate currentDate = null; /* parametro che rappresenta la data attuale */

    boolean isToday = false;

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
        userDAO = daoFactory.getUserDAO();


        /* setto l'id della struttura sulla base dell'id ricevuto */
        idStructure = Long.valueOf(request.getParameter("idStructure"));

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
    /* verifico se la data di prenotazione e' uguale alla data attuale del client */
    isToday = pickedDate.isEqual(currentDate);

    freeSlots = new ArrayList<LocalTime>();


    for (indexTime = LocalTime.of(openingTime.getHour() , openingTime.getMinute(), openingTime.getSecond());
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

%>
            { "result": "<%=result%>",
              "availableTimes":[ <% for(i = 0; i < freeSlots.size(); i++) { %>
                                      "<%=freeSlots.get(i)%>"<%=i!=freeSlots.size()-1?",":""%>
                                      <%}%>
                               ]
            }
