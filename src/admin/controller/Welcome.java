package admin.controller;

import model.dao.DAOFactory;
import model.dao.StatisticsDAO;
import model.dao.UserDAO;
import model.mo.StatisticsEarnings;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

public class Welcome {

    private Welcome() {
    }

    public static void welcome(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set data to rappresent on welcome page then call the admin/view.jsp
         * */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        String applicationMessage = null;
        UserDAO sessionUserDAO = null;
        UserDAO userDAO = null;
        User loggedUser = null;
        User admin = null;
        StatisticsDAO statisticsDAO = null;
        StatisticsEarnings statisticsEarnings = null;
        TreeMap<Time,Integer> totalAppointmentGroupByHourStart = null;

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Acquisisco un DAOFactory per poter lavorare sul DB*/
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            admin = userDAO.findById(loggedUser.getId());

            statisticsDAO = daoFactory.getStatisticsDAO();

            statisticsEarnings = statisticsDAO.totalEarningsWithAndWithoutDiscount();

            totalAppointmentGroupByHourStart = statisticsDAO.totalAppointmentGroupByHourStartInAYear(LocalDate.now());

            /* Commit sul db */
            daoFactory.commitTransaction();
            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT DELLA TRANSAZIONE");
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
                System.err.println("ERRORE NEL ROLLBACK DELLA TRANSAZIONE");
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
        /* 3) Application messagge da mostrare all'utente */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "admin/view");
        /* 5) Setto il totale guadagni applicando gli sconti e non applicandoli */
        request.setAttribute("statisticsEarnings", statisticsEarnings);
        /* 6) Setto l'hashmap con il numero totale di appuntamenti in un anno per ogni fascia oraria presente nel db */
        request.setAttribute("totalAppointmentGroupByHourStart",totalAppointmentGroupByHourStart);
        /* 7) Setto l'oggetto admin da cui estrarre le informazioni utili */
        request.setAttribute("admin",admin);

    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }


}
