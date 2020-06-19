package admin.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.OrdersDAO;
import model.dao.UserDAO;
import model.mo.Order;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class Logistics {

    private Logistics() {

    }

    public static void showLogistics(HttpServletRequest request, HttpServletResponse response) {
        /**
         * .
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

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            Long pageToShow = 1L;
            if (request.getParameter("pageToShow") != null) {
                /* Ovvero ho cliccato sulla bar di pagination */
                pageToShow = Long.valueOf(request.getParameter("pageToShow"));
            }

            commonView(daoFactory, pageToShow, request);

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
        /* 3) Setto gli attributi della request che verranno processati dalla show-logistics.jsp */
        request.setAttribute("viewUrl", "admin/show-logistics");


    }

    public static void modifyStatus(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Modify status of specified order
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        OrdersDAO ordersDAO = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        Long idOrderToModify = null;
        String status = null;
        LocalDate sellDate = null;

        boolean modified = false;

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

            Long pageToShow = 1L;
            if (request.getParameter("pageToShow") != null) {
                /* Ovvero ho cliccato sulla bar di pagination */
                pageToShow = Long.valueOf(request.getParameter("pageToShow"));
            }

            ordersDAO = daoFactory.getOrdersDAO();

            /* setto l'id dell'ordine da modificare sulla base dell'id ricevuto */
            idOrderToModify = Long.valueOf(request.getParameter("idOrder"));

            /* setto lo status dell'ordine da modificare sulla base dello status ricevuto */
            status = request.getParameter("status");

            /* se lo status è superiore o uguale a SENT flaggo l'ordine come già venduto */
            if (status.equals(StaticFunc.SENT)) {
                sellDate = LocalDate.parse(request.getParameter("sellDate"));
            } else if (status.equals(StaticFunc.DELIVERING)) {
                sellDate = LocalDate.parse(request.getParameter("sellDate"));
            } else if (status.equals(StaticFunc.DELIVERED)) {
                sellDate = LocalDate.parse(request.getParameter("sellDate"));
            } else if (status.equals(StaticFunc.CANCELED)) {
                sellDate = LocalDate.parse(request.getParameter("sellDate"));
            }

            modified = ordersDAO.modifyStatusById(idOrderToModify, status, sellDate);

            commonView(daoFactory, pageToShow, request); /* setto l'attributo "logisticOrders" all'interno della request */

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (modified) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che l'ordine è stato modificato */
                applicationMessage = "Status order modified SUCCESSFULLY.";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione l'ordine non è stato cancellato .*/
                applicationMessage = "Error: this order's status could not be modified.";
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
        /* 3) Setto gli attributi della request che verranno processati dalla show-logistics.jsp */
        request.setAttribute("viewUrl", "admin/show-logistics");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (modified) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

    }

    public static void commonView(DAOFactory daoFactory, Long pageToShow, HttpServletRequest request) {

        OrdersDAO ordersDAO = daoFactory.getOrdersDAO();
        Long offset = (pageToShow - 1) * Configuration.TOT_REC_TO_SHOW_LOGISTICS;
        int adjacents = 2;

        Long totalRecords = ordersDAO.countOrdersForLogistics();
        Long totalNumberOfPages = (long) (Math.ceil(totalRecords.doubleValue() / Configuration.TOT_REC_TO_SHOW_LOGISTICS.doubleValue()));
        Long secondLast = totalNumberOfPages - 1; /* total pages minus 1 */

        ArrayList<Order> logisticOrders = ordersDAO.fetchRangeOfOrdersForLogistics(offset);

        /* 4) Setto l'array list di ordini da mostrare all'admin */
        request.setAttribute("logisticOrders", logisticOrders);
        /* 5) Setto il numero totale di pagine da mostrare */
        request.setAttribute("totalNumberOfPages", totalNumberOfPages);
        /* 6) Ritorno indietro il numero di pagina che si è scelto di visualizzare */
        request.setAttribute("currentPage",pageToShow); /* la pageToShow ovvero la pagina che si è scelta di visualizzare diventa la pagina corrente */
    }
}
