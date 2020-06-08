package home.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.OrdersDAO;
import model.dao.UserDAO;
import model.mo.Order;
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
        User loggedUser = null;


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

            commonView(daoFactory, loggedUser, request);

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


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/orders");

    }

    public static void cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then CANCEL order.
         */


        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        OrdersDAO ordersDAO = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        Long idOrderToCancel = null;

        boolean canceled = false;

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

            ordersDAO = daoFactory.getOrdersDAO();

            /* setto l'id dell'ordine da cancellare sulla base dell'id ricevuto */
            idOrderToCancel = Long.valueOf(request.getParameter("idOrder"));

            canceled = ordersDAO.modifyStatusById(idOrderToCancel, StaticFunc.CANCELED);

            commonView(daoFactory, loggedUser, request); /* setto l'attributo "orders" all'interno della request */

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (canceled) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che l'ordine è stato cancellato */
                applicationMessage = "Order canceled SUCCESSFULLY.";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

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

    public static void commonView(DAOFactory daoFactory, User loggedUser, HttpServletRequest request) {
        OrdersDAO ordersDAO = daoFactory.getOrdersDAO();

        /* Prendo TUTTI gli ordini del cliente loggato TODO: fare una visualizzazione per pagine?*/
        ArrayList<Order> orders = ordersDAO.fetchOrdersByCustomerId(loggedUser.getId());

        /* Per ogni ordine setto la sua itemList */
        for (Order order : orders) {
            order.setItemList(ordersDAO.listOrderedProducts(order.getId()));
        }

        /* 4) Setto l'ArrayList di ordini da mostrare in base a quale utente è loggato */
        request.setAttribute("orders", orders);

    }
}
