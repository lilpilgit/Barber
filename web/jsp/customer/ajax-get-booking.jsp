<%@ page import="model.dao.BookingDAO"%>
<%@ page import="model.dao.DAOFactory"%>
<%@ page import="model.dao.StructureDAO"%>
<%@ page import="model.dao.UserDAO"%>
<%@ page import="model.mo.Booking"%>
<%@ page import="model.mo.Structure"%>
<%@ page import="model.mo.User"%>
<%@ page import="services.config.Configuration"%>
<%@ page import="java.util.ArrayList"%><%@ page import="java.util.HashMap"%>
<%@ page contentType="text/plain" pageEncoding="UTF-8"%>

<%
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
    UserDAO userDAO = null;
    User user = null;
    BookingDAO bookingDAO = null;
    Booking booking = null;
    ArrayList<Booking> bookings = null;
    String result = "fail"; /* Se tutto va a buon fine, poi diventera' success */

    boolean alreadyBooked = false;

    Long idCustomer = null; /* parametro che indica l'id del cliente che sta cliccando su booking */

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

        daoFactory.beginTransaction();

        bookingDAO = daoFactory.getBookingDAO();

        idCustomer = Long.valueOf(request.getParameter("idCustomer"));

        alreadyBooked = bookingDAO.alreadyBooked(loggedUser);

        /* Se non sono stati effettuati appuntamenti, eseguo la getLastBooking che mi trova l'ultimo appuntamento */
        if (alreadyBooked) {
        structureDAO = daoFactory.getStructureDAO();

        /* Faccio il fetch dell'unica struttura che ho nel db */
        structure = structureDAO.fetchStructure();

        /* Creo l'oggetto booking che conterra' tutte le informazioni riferite allo status dell'ultimo appuntamento */
        booking = bookingDAO.getLastBooking(idCustomer, structure.getId());
        }

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

    result = "success";

%>

    {
    	"result": "<%=result%>",
    	"alreadyBooked":"<%=alreadyBooked%>"<%if (alreadyBooked) {%>,
    	"idBooking": "<%=booking.getId()%>",
    	"deleted": "<%=booking.isDeleted()%>",
    	"deletedReason": "<%=booking.getDeletedReason()%>",
    	"date": "<%=booking.getDate()%>",
    	"hourStart": "<%=booking.getHourStart()%>"<%}%>
    }
