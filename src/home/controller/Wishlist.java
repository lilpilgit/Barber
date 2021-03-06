package home.controller;

import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.dao.WishlistDAO;
import model.mo.ExtendedProduct;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Wishlist {

    private Wishlist() {
    }

    public static void showWishlist(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call wishlist.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        ArrayList<Product> wishlist = null; //la wishlist da passare alla jsp
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
            request.setAttribute("viewUrl", "customer/wishlist");
        }
    }

    public static void addToWishlist(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then add product to wishlist.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        WishlistDAO wishlistDAO = null;
        User user = null;
        Long idProductToAdd = null; /* il del prodotto da aggiungere alla wishlist */
        String from = null; /* da quale jsp viene chiamato il metodo */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean added = false;
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

            user = userDAO.findById(loggedUser.getId());

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(user)) {
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
                /* setto l'id del prodotto da aggiungere alla wishlist sulla base dell'id ricevuto */
                idProductToAdd = Long.valueOf(request.getParameter("idProduct"));

                wishlistDAO = daoFactory.getWishlistDAO();

                added = wishlistDAO.addProductToWishlist(user, idProductToAdd);

                /* IMPORTANTE !!! chiamo la commonView solo dopo che è stato aggiunto il prodotto altrimenti viene falsato il parametro inWishlist */

                /* tale metodo può essere chiamato da diverse pagine, posso capire la jsp dalla quale è stato chiamato sulla
                 *  base del parametro "from" */
                from = request.getParameter("from");
                if (from != null) {
                    if (from.equals("product")) {
                        /* aggiungo il prodotto alla wishlist dalla pagina product.jsp */
                        /* 6) setto gli attributi "product" e "inWishlist" da mostrare nuovamente nella pagina product.jsp*/
                        home.controller.Product.commonView(daoFactory, loggedUser, idProductToAdd, request);
                        System.err.println("AGGIUNTO PRODOTTO ALLA WISHLIST DA DENTRO product.jsp");

                    } else if (from.equals("cart")) {
                    /* aggiunto il prodotto alla wishlist da dentro la pagina cart.jsp /

                    /* aggiunge l'oggetto "cart" come ArrayList alla request */
                        Cart.commonView(daoFactory, loggedUser, request);
                        System.err.println("AGGIUNTO PRODOTTO ALLA WISHLIST DA DENTRO cart.jsp");
                    }

                } else {
                    throw new RuntimeException("Wishlist.java ==> non è stato passato alcun parametro 'from'");
                }

                if (added) {
                    /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto è stato aggiunto alla wishlist dell'utente */
                    applicationMessage = "Product added to wishlist SUCCESSFULLY.";
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
                /* Se viene fatto il rollback della transazione il prodotto non è stato aggiunto .*/
                applicationMessage = "Error: this product could not be added to your wishlist.";
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

            /* 5) l'url della pagina da visualizzare dopo aver effettuato l'inserimento */

            if (from != null) {
                if (from.equals("product")) {
                    /* aggiungo il prodotto alla wishlist dalla pagina product.jsp */
                    request.setAttribute("viewUrl", "common/product");

                } else if (from.equals("cart")) {
                    /* aggiunto il prodotto alla wishlist da dentro la pagina cart.jsp */
                    request.setAttribute("viewUrl", "customer/cart");
                }

            } else {
                request.setAttribute("viewUrl", "common/home");
            }
        }

    }

    public static void removeFromWishlist(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Fetch user logged then remove product from wishlist.
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO userDAO = null;
        WishlistDAO wishlistDAO = null;
        User user = null;
        Long idProductToRemove = null; /* il del prodotto da rimuovere dalla wishlist */
        String from = null; /* da quale jsp viene chiamato il metodo */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean removed = false;
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

            user = userDAO.findById(loggedUser.getId());

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(user)) {
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
                /* setto l'id del prodotto da rimuovere dal carrello sulla base dell'id ricevuto */
                idProductToRemove = Long.valueOf(request.getParameter("idProduct"));

                wishlistDAO = daoFactory.getWishlistDAO();

                removed = wishlistDAO.removeProductFromWishlist(user, idProductToRemove);

                /* tale metodo può essere chiamato da diverse pagine, posso capire la jsp dalla quale è stato chiamato sulla
                 *  base del parametro "from" */
                from = request.getParameter("from");
                if (from != null) {
                    if (from.equals("cart")) {
                        /* è stato tolto il prodotto cliccando la stellina dentro la pagina cart.jsp */
                        Cart.commonView(daoFactory, loggedUser, request); /* aggiungo l'oggetto "cart" che è un ArrayList alla cart.jsp */
                        System.err.println("RIMOSSO PRODOTTO DALLA WISHLIST DA DENTRO cart.jsp");

                    } else if (from.equals("wishlist")) {
                        commonView(daoFactory, loggedUser, request); /* setto l'attributo "wishlist" all'interno della request */
                        System.err.println("RIMOSSO PRODOTTO DALLA WISHLIST DA DENTRO wishlist.jsp");
                    } else if (from.equals("product")) {
                        /* aggiungo l'oggetto "product" e "inWishlist" al'interno della request per sapere quale oggetto visualizzare e se è già nella wishlist */
                        home.controller.Product.commonView(daoFactory, loggedUser, idProductToRemove, request);
                        System.err.println("RIMOSSO PRODOTTO DALLA WISHLIST DA DENTRO product.jsp");
                    }

                }

                if (removed) {
                    /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto è stato rimosso dalla wishlist dell'utente */
                    applicationMessage = "Product removed from wishlist SUCCESSFULLY.";
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
                /* Se viene fatto il rollback della transazione il prodotto non è stato rimosso .*/
                applicationMessage = "Error: this product could not be removed from your wishlist.";
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
            /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione dalla wishlist */
            if (from != null) {
                if (from.equals("cart")) {
                    /* decido di rimanere nella pagina cart.jsp*/
                    request.setAttribute("viewUrl", "customer/cart");
                } else if (from.equals("wishlist")) {
                    request.setAttribute("viewUrl", "customer/wishlist");
                } else if (from.equals("product")) {
                    request.setAttribute("viewUrl", "common/product");
                }

            }
            /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (removed) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
        }

    }

    public static void commonView(DAOFactory daoFactory, User loggedUser, HttpServletRequest request) {

        ArrayList<ExtendedProduct> wishlist = null; //il carrello da passare alla jsp
        UserDAO userDAO = daoFactory.getUserDAO();
        WishlistDAO wishlistDAO = daoFactory.getWishlistDAO();
        User user = null;
        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = null;

        user = userDAO.findById(loggedUser.getId());

        /* setto l'oggetto wishlist all'interno dell'oggetto utente */
        wishlist = wishlistDAO.fetchWishlist(user);

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

        /* Setto la wishlist da mostrare nella pagina della wishlist dell'utente loggato */
        request.setAttribute("wishlist", wishlist);

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
