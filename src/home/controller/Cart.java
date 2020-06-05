package home.controller;

import model.dao.CartDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.ExtendedProduct;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Cart {

    private Cart() {
    }

    public static void showCart(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user then call cart.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */


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
        request.setAttribute("viewUrl", "customer/cart");
    }

    public static void addToCart(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then add product to cart with desired quantity.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        CartDAO cartDAO = null;
        User user = null;
        Long idProductToAdd = null; /* il del prodotto da aggiungere al carrello */
        Integer desiredQty = 1; /* quantità desiderata da essere aggiunta al carrello : di default a 1 se viene aggiunto al carrello dalla pagina wishlist.jsp */
        String from = null; /* da quale jsp viene chiamato il metodo */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */

        boolean added = false;

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
            idProductToAdd = Long.valueOf(request.getParameter("idProduct"));

            cartDAO = daoFactory.getCartDAO();

            /* tale metodo può essere chiamato da diverse pagine, posso capire la jsp dalla quale è stato chiamato sulla
             *  base del parametro "from" */
            from = request.getParameter("from");
            if (from != null) {
                if (from.equals("product")) {
                    /* prendo la quantità scelta da aggiungere al carrello */
                    desiredQty = Integer.parseInt(request.getParameter("desiredQty"));
                    added = cartDAO.addProductToCart(user, idProductToAdd, desiredQty);
                    /* 6) setto gli attributi "product" e "inWishlist" da mostrare nuovamente nella pagina product.jsp*/
                    Product.commonView(daoFactory, loggedUser, idProductToAdd, request);
                    System.err.println("AGGIUNTO PRODOTTO AL CARRELLO DA DENTRO product.jsp");

                } else if (from.equals("wishlist")) {
                    /* aggiunto il prodotto al carrello da dentro la pagina wishlist dunque quantità di default = 1*/
                    added = cartDAO.addProductToCart(user, idProductToAdd, desiredQty);
                    /* aggiunge la wishlist come ArrayList alla request*/
                    Wishlist.commonView(daoFactory, loggedUser, request);
                    System.err.println("AGGIUNTO PRODOTTO AL CARRELLO DA DENTRO wishlist.jsp");
                }

            } else {
                throw new RuntimeException("Cart.java ==> non è stato passato alcun parametro 'from'");
            }


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (added) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto è stato aggiunto al carrello dell'utente */
                applicationMessage = "Product added to cart SUCCESSFULLY.";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione il prodotto non è stato aggiunto .*/
                applicationMessage = "Error: this product could not be added to your cart.";
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
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (added) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }


        /* 5) l'url della pagina da visualizzare dopo aver effettuato l'inserimento  */
        if (from != null) {
            if (from.equals("product")) {
                request.setAttribute("viewUrl", "common/product");

            } else if (from.equals("wishlist")) {
                request.setAttribute("viewUrl", "customer/wishlist");
            }
        } else {
            request.setAttribute("viewUrl", "common/home");
        }

    }

    public static void removeFromCart(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then remove product from cart.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        CartDAO cartDAO = null;
        User user = null;
        Long idProductToAdd = null; /* il del prodotto da aggiungere al carrello */
        ArrayList<ExtendedProduct> cart = null; //il carrello da passare alla jsp
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */

        boolean removed = false;

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
            idProductToAdd = Long.valueOf(request.getParameter("idProduct"));

            cartDAO = daoFactory.getCartDAO();

            removed = cartDAO.removeProductFromCart(user, idProductToAdd);

            commonView(daoFactory, loggedUser, request); /* setto l'attributo "cart" all'interno della request */

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (removed) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto è stato rimosso dal carrello dell'utente */
                applicationMessage = "Product removed from cart SUCCESSFULLY.";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                /* Se viene fatto il rollback della transazione il prodotto non è stato rimosso .*/
                applicationMessage = "Error: this product could not be removed from your cart.";
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
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo impiegato */
        request.setAttribute("viewUrl", "customer/cart");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (removed) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

    }

    public static void commonView(DAOFactory daoFactory, User loggedUser, HttpServletRequest request) {

        /**
         * Set attribute "cart" inside request
         */
        ArrayList<ExtendedProduct> cart = null; //il carrello da passare alla jsp
        CartDAO cartDAO = daoFactory.getCartDAO();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = null;

        user = userDAO.findById(loggedUser.getId());

        /* setto l'oggetto carrello all'interno dell'oggetto utente */
        cart = cartDAO.fetchCart(user);


        /* Setto il carrello da mostrare nella pagina del carrello dell'utente loggato */
        request.setAttribute("cart", cart);


    }
}
