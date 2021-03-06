package home.controller;

import functions.StaticFunc;
import model.dao.*;
import model.mo.ExtendedProduct;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;
import org.json.JSONArray;
import org.json.JSONObject;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class Checkout {
    private Checkout() {
    }

    public static void showCheckout(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call checkout.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        User customer = null;
        UserDAO userDAO = null;
        ArrayList<ExtendedProduct> checkoutProducts = new ArrayList<>(); /* prodotti da mostrare nella pagina di checkout */
        BigDecimal totalPrice = null;
        BigDecimal totalSaved = null;
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
                customer = userDAO.findById(loggedUser.getId()); /* per avere maggiori informazioni riguardo il cliente */

                String checkoutInfo = request.getParameter("checkoutInfo");

                System.err.println("checkoutInfo ===> " + checkoutInfo);

                JSONObject mainObj = new JSONObject(checkoutInfo);
                /* prezzo totale al momento del checkout che assumiamo non venga variato anche se nel frattempo variano i prezzi */
                totalPrice = new BigDecimal((String) mainObj.get("totalPrice"));
                totalSaved = new BigDecimal((String) mainObj.get("totalSaved"));

                /* array di prodotti da acquistare del tipo ==>
                "productsToBuy":[{"ID":"1","desiredQty":"1","name":"Avaha","eachFinalPrice":"10"},
                                 {"ID":"4","desiredQty":"4","name":"Shavette","eachFinalPrice":"50"},
                                 {"ID":"5","desiredQty":"2","name":"Shaving milk","eachFinalPrice":"20"}]
                */

                JSONArray productsToBuyArray = (JSONArray) mainObj.get("productsToBuy");

                for (int i = 0; i < productsToBuyArray.length(); i++) {
                    JSONObject productObj = (JSONObject) productsToBuyArray.get(i); /*il singolo oggetto product del tipo {"ID":"1","desiredQty":"1"} */
                    Long ID = Long.valueOf((String) productObj.get("ID"));
                    Integer desiredQty = Integer.valueOf((String) productObj.get("desiredQty"));
                    String name = (String) productObj.get("name");
                    BigDecimal eachFinalPrice = new BigDecimal((String) productObj.get("eachFinalPrice"));

                    System.out.println("ID=" + ID + " | desiredQty=" + desiredQty + " | name=" + name + " | eachFinalPrice=" + eachFinalPrice);

                    ExtendedProduct productToShow = new ExtendedProduct();
                    productToShow.setId(ID);
                    productToShow.setRequiredQuantity(desiredQty);
                    productToShow.setName(name);
                    productToShow.setPrice(eachFinalPrice);

                    checkoutProducts.add(productToShow); /* aggiungo all'array list dei prodotti da mostrare nel riepilogo */
                }
                System.out.println("totalPrice:" + totalPrice + " | totalSaved:" + totalSaved);

                commonView(daoFactory, request);

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
            request.setAttribute("viewUrl", "customer/checkout");
            /* 4) Lista dei prodotti da mostrare nel riepilogo del checkout */
            request.setAttribute("checkoutProducts", checkoutProducts);
            /* 5) Prezzo totale da pagare */
            request.setAttribute("totalPrice", totalPrice);
            /* 6) Totale risparmiato grazie agli sconti */
            request.setAttribute("totalSaved", totalSaved);
            /* 7) Oggetto utente per sapere ulteriori informazioni riguardo il cliente */
            request.setAttribute("customer", customer);
        }
    }

    public static void processCheckout(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call result-checkout.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        ProductDAO productDAO = null;
        OrdersDAO ordersDAO = null;
        UserDAO userDAO = null;
        User customer = null;
        CartDAO cartDAO = null; /* necessario per poter rimuovere i prodotti acquistati */
        ArrayList<ExtendedProduct> items = new ArrayList<>(); /* prodotti da aggiungere alla ITEMS_LIST */
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;
        boolean removedFromCart = true; /* posto a true, scatta a false se almeno uno dei prodotti non è stato rimosso dal carrello dopo l'ordine */
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
                productDAO = daoFactory.getProductDAO();

                ordersDAO = daoFactory.getOrdersDAO();

                cartDAO = daoFactory.getCartDAO();

                customer = userDAO.findById(loggedUser.getId());

                /* fetch dei parametri passati dalla pagina checkout.jsp */
                BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
                String[] ids = request.getParameterValues("ids");
                String[] quantities = request.getParameterValues("quantities");
                String state = request.getParameter("state");
                String region = request.getParameter("region");
                String city = request.getParameter("city");
                String cap = request.getParameter("cap");
                String street = request.getParameter("street");
                String house_number = request.getParameter("house_number");

                /* aggiungo le informazioni riguardanti la spedizione */
                customer.setAddress(StaticFunc.formatFinalAddress(state, region, city, street, cap, house_number));

                for (int i = 0; i < ids.length; i++) {
                    System.out.println("ID " + i + " ==> " + ids[i] + " | quantities ==> " + quantities[i]);
                    /* per ogni id presente nell'array String[] lo converto in Long e ricerco il prodotto nel DB tramite tale ID */
                    Product product = productDAO.findProductById(Long.valueOf(ids[i]));
                    ExtendedProduct extendedProduct = new ExtendedProduct(product);
                    /* setto la quantità desiderata tramite l'array quantities */
                    extendedProduct.setRequiredQuantity(Integer.valueOf(quantities[i]));
                    /* l'aggiungo all'array list di items da passare al metodo insert */
                    items.add(extendedProduct);
                }

                /* Effettuo l'inserimento del nuovo ordine nella tabella degli ordini */
                ordersDAO.insert(customer, totalPrice, items);
                inserted = true; /* Se non viene sollevata l'eccezione, è stato inserito correttamente*/

                if (inserted) {
                    /* se gli ordini sono andati a buon fine devo rimuovere i prodotti dal carrello dell'utente */
                    for (ExtendedProduct boughtItem : items) {
                        removedFromCart = removedFromCart && cartDAO.removeProductFromCart(loggedUser, boughtItem.getId());
                    }
                }

                commonView(daoFactory, request);
            }

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit sul db */
            daoFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if(!inserted || !removedFromCart){
                /* Se c'è stato un errore... */
                applicationMessage = "Order not received correctly.";
            }

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
            request.setAttribute("viewUrl", "customer/result-checkout");
            /* 4) il messaggio da visualizzare */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
            if (inserted && removedFromCart) {
                /* SUCCESS */
                request.setAttribute("result", "success");
            } else {
                /* FAIL */
                request.setAttribute("result", "fail");
            }
        }
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {

        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = null;

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

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
