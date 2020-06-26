package home.controller;

import model.dao.*;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Product {

    private Product() {
    }

    public static void showProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call product.jsp with id of product to show.
         */

        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        UserDAO userDAO = null;
        User loggedUser = null;
        boolean cookieValid = true;


        String applicationMessage = null;

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
                    home.controller.Home.logout(request, response);
                    cookieValid = false;
                }
            } else {
                /* La pagina che mostra il dettaglio di ciascun prodotto è pubblica */
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Eseguo la logica di business */

                /* Fetching dei parametri  */
                String idProduct = request.getParameter("idProduct");
                Long id = 1L;
                if (idProduct != null) {
                    /* posso provare a parsarlo per evitare NullPointerException*/
                    id = Long.parseLong(idProduct);
                }
                System.err.println("id:" + id);

                commonView(daoFactory, loggedUser, id, request);
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
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/product");
        }
    }

    public static void commonView(DAOFactory daoFactory, User loggedUser, Long id, HttpServletRequest request) {

        ProductDAO productDAO = daoFactory.getProductDAO(); /* per fetchare il prodotto */
        WishlistDAO wishlistDAO = daoFactory.getWishlistDAO();
        CartDAO cartDAO = daoFactory.getCartDAO();
        model.mo.Product product = null; /* prodotto fetchato dal db da mostrare nella pagina product.jsp */
        boolean inWishlist = false; /* flag per sapere se il prodotto è nella wishlist dell'utente loggato */
        boolean inCart = false; /* flag per sapere se il prodotto e' nel carrello dell'utente loggato */

        product = productDAO.findProductById(id);

        System.err.println("product to show " + product);

        if (loggedUser != null) {
            /* controllo necessario altrimenti va in errore se non si è utenti loggati e si prova a visualizzare la pagina product */
            inWishlist = wishlistDAO.inWishlist(loggedUser, id);
            inCart = cartDAO.inCart(loggedUser, id);
        }

        /* 5) Setto il prodotto da mostrare */
        request.setAttribute("product", product);
        /* 6) Setto il flag per sapere se il prodotto è in wishlist già per l'utente loggato */
        request.setAttribute("inWishlist", inWishlist);
        /* 7) Setto il flag per sapere se il prodotto è in wishlist già per l'utente loggato */
        request.setAttribute("inCart", inCart);

    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }


}
