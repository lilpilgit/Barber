<%@ page import="model.dao.CartDAO" %>
<%@ page import="model.dao.DAOFactory" %>
<%@ page import="model.dao.UserDAO" %>
<%@ page import="model.mo.User" %>
<%@ page import="services.config.Configuration" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/plain" pageEncoding="UTF-8"%>
<%@ page errorPage="../error/404.jsp" %>
<%@ page session="false" %>
<%
    /**
     * Fetch user logged then change desired quantity of product into the cart.
     *
     * WARNING! This method is compatible only with AJAX request.
     */

    DAOFactory sessionDAOFactory = null; //per i cookie
    DAOFactory daoFactory = null; //per il db
    User loggedUser = null;
    UserDAO userDAO = null;
    CartDAO cartDAO = null;
    User user = null;
    Long idProductToChange = null; /* il del prodotto di cui modificare la quantità desiderata nel carrello */
    String operation = null; /* parametro che può assumere i valori "increase" o "decrease" per indicare l'operazione da eseguire sul DB */
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

        userDAO = daoFactory.getUserDAO();

        user = userDAO.findById(loggedUser.getId());

        /* setto l'id del prodotto da aggiungere al carrello sulla base dell'id ricevuto */
        idProductToChange = Long.valueOf(request.getParameter("idProduct"));

        cartDAO = daoFactory.getCartDAO();

        /* setto l'id del prodotto da aggiungere al carrello sulla base dell'id ricevuto */
        operation = request.getParameter("operation");

        if(operation != null){
            if(operation.equals("increase")){
                /* incremento la quantità desiderata */
                changed = cartDAO.changeDesiredQuantity(user,idProductToChange,true,1);
                System.err.println("CART.JAVA ==> Incrementata la quantità desiderata");
            }else{
                /* decremento la quantità desiderata */
                changed = cartDAO.changeDesiredQuantity(user,idProductToChange,false,1);
                System.err.println("CART.JAVA ==> Decrementata la quantità desiderata");
            }
        }

        /* Commit fittizio */
        sessionDAOFactory.commitTransaction();

        /* Commit sul db */
        daoFactory.commitTransaction();

        System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

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
%>

<%        if (changed) {
            /* SUCCESS */
            %>
            {"result":"success"}
        <%} else {
            /* FAIL */%>

            {"result":"fail"}
        <%}%>
