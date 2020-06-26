package home.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.UserDAO;
import model.mo.Product;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Shop {
    private Shop() {
    }

    public static void showShop(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call shop.jsp
         */

        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        ProductDAO productDAO = null; /* per fetchare i prodotti */
        UserDAO userDAO = null;
        User loggedUser = null;
        ArrayList<Product> products = null; /* prodotti fetchati dal db da mostrare nella pagina shop */
        ArrayList<String> categories = null; /*categorie da mostrare nel dropdown del filtro */
        ArrayList<String> brands = null; /*produttori da mostrare nel dropdown del filtro */
        String categoryToFilter = "All"; /* voce predefinita nel filtro delle categorie */
        String brandToFilter = "All"; /* voce predefinita nel filtro dei brands */
        boolean cookieValid = true;

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();


            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/

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
                    home.controller.Home.logout(request, response); /* loggedOn false, loggedUser null, viewUrl ==> home*/
                    cookieValid = false;
                }
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Istanzio un DAO per poter fetchare i prodotti */
                productDAO = daoFactory.getProductDAO();

                /* Prendo tutte le categorie dal database */
                categories = productDAO.findAllCategories();

                /* Prendo tutti i produttori dal database */
                brands = productDAO.findAllProducers();

//                /* Fetching dei parametri  */
//                String toFilter = request.getParameter("filter");
//                System.err.println("toFilter ==>" + toFilter);

//                int filter = 0; /* ipotizzo che non venga richiesto il filtraggio dei prodotti */

//                if (toFilter != null) { /* NON È LA PRIMA VOLTA CHE ARRIVO SULLA PAGINA SHOP.JSP OPPURE NON VOGLIO FILTRARE */
//                    /* posso provare a parsarlo per evitare NullPointerException*/
//                    filter = Integer.parseInt(toFilter);
//                }
//                System.err.println("filter:" + filter);

                if (request.getParameter("brand") != null) {
                    brandToFilter = request.getParameter("brand");
                }
                if (request.getParameter("category") != null) {
                    categoryToFilter = request.getParameter("category");
                }

                System.err.println("categoryToFilter" + categoryToFilter);
                System.err.println("brandToFilter" + brandToFilter);

                if (brandToFilter.equals("All") && categoryToFilter.equals("All")) {

                    products = productDAO.findAllProducts();

                } else {
                    products = productDAO.findFilteredProducts((categoryToFilter.equals("All")) ? "%" : categoryToFilter, (brandToFilter.equals("All")) ? "%" : brandToFilter);

                }
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


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
            System.err.println("loggedOn==>" + loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/shop");
            /* 4) Setto la lista dei prodotti da mostrare */
            request.setAttribute("products", products);
            /* 5) Setto la lista delle categorie completa */
            request.setAttribute("categories", categories);
            /* 6) Setto la lista dei brand completa */
            request.setAttribute("brands", brands);
            /* 7) Setto il brand che era stato selezionato per poterlo mostrare nella pagina filtrata all'interno del dropdown */
            request.setAttribute("brandFiltered", brandToFilter);
            /* 8) Setto la categoria che era stata selezionata per poterla mostrare nella pagina filtrata all'interno del dropdown */
            request.setAttribute("categoryFiltered", categoryToFilter);
        }

    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }

}
