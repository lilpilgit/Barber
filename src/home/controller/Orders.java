package home.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.OrdersDAO;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.mo.Order;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Orders {
    private Orders() {
    }

    public static void showOrders(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call orders.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO userDAO = null;
        User loggedUser = null;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

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
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "customer/orders");
        }
    }

    public static void cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then CANCEL order.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        OrdersDAO ordersDAO = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        Long idOrderToCancel = null;
        boolean canceled = false;
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

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
                /* Eseguo la logica di business */
                ordersDAO = daoFactory.getOrdersDAO();

                /* setto l'id dell'ordine da cancellare sulla base dell'id ricevuto */
                idOrderToCancel = Long.valueOf(request.getParameter("idOrder"));

                canceled = ordersDAO.modifyStatusById(idOrderToCancel, StaticFunc.CANCELED, null);

                commonView(daoFactory, loggedUser, request); /* setto l'attributo "orders" all'interno della request */

            }

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (canceled) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che l'ordine è stato cancellato */
                applicationMessage = "Order canceled SUCCESSFULLY.";
            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione l'ordine non è stato cancellato .*/
                applicationMessage = "Error: this order could not be canceled. Contact us for help.";
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");

            }
            throw new RuntimeException(e);

        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        if (cookieValid) {

            /* Setto gli attributi della request che verranno processati dalla profile.jsp */

            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) il messaggio da visualizzare  */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) l'url della pagina da visualizzare */
            request.setAttribute("viewUrl", "customer/orders");
            /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (canceled) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
        }

    }

    public static void commonView(DAOFactory daoFactory, User loggedUser, HttpServletRequest request) {

        OrdersDAO ordersDAO = daoFactory.getOrdersDAO();
        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = null;

        /* Prendo TUTTI gli ordini del cliente loggato */
        ArrayList<Order> orders = ordersDAO.fetchOrdersByCustomerId(loggedUser.getId());

        /* Per ogni ordine setto la sua itemList */
        for (Order order : orders) {
            order.setItemList(ordersDAO.listOrderedProducts(order.getId()));
        }

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

        /* 4) Setto l'ArrayList di ordini da mostrare in base a quale utente è loggato */
        request.setAttribute("orders", orders);

        /* Setto l'oggetto struttura da mostrare in ogni footer dell'area customer */
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
