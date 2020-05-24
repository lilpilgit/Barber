package home.controller;

public class Cart {

    private Cart() {

    }
//
//    public static void view(HttpServletRequest request, HttpServletResponse response) {
//        /**
//         * Check if user is logged then call the common/home.jsp
//         * */
//        DAOFactory sessionDAOFactory = null; //per i cookie
//        DAOFactory daoFactory = null; //per il db
//        User loggedUser = null;
//
//        try {
//            /* Inizializzo il cookie di sessione */
//            HashMap sessionFactoryParameters = new HashMap<String, Object>();
//            sessionFactoryParameters.put("request", request);
//            sessionFactoryParameters.put("response", response);
//            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
//
//            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
//             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
//            sessionDAOFactory.beginTransaction();
//
//            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/
//
//            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
//            loggedUser = sessionUserDAO.findLoggedUser();
//
//            /* DAOFactory per manipolare i dati sul DB */
//            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
//
//            /* Inizio la transazione sul Database*/
//            daoFactory.beginTransaction();
//
//            /* Commit della transazione sul db */
//            daoFactory.commitTransaction();
//
//            /* Commit fittizio */
//            sessionDAOFactory.commitTransaction();
//
//
//            /* 1) Attributo che indica se è loggato oppure no */
//            request.setAttribute("loggedOn", loggedUser != null);
//            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
//            request.setAttribute("loggedUser", loggedUser);
//            /* 3) Setto quale view devo mostrare */
//            request.setAttribute("viewUrl", "customer/cart");
//        } catch (Exception e) {
//            try {
//                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
//                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
//            } catch (Throwable t) {
//            }
//            throw new RuntimeException(e);
//
//        } finally {
//            try {
//                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
//                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
//            } catch (Throwable t) {
//            }
//        }
//
//    }

}
