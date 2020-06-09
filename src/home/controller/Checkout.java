package home.controller;

import model.dao.*;
import model.mo.ExtendedProduct;
import model.mo.Product;
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
        User customer;
        UserDAO userDAO = null;
        ArrayList<ExtendedProduct> checkoutProducts = new ArrayList<>(); /* prodotti da mostrare nella pagina di checkout */
        BigDecimal totalPrice = null;
        BigDecimal totalSaved = null;
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

            userDAO = daoFactory.getUserDAO();

            customer = userDAO.findById(loggedUser.getId()); /* per avere maggiori informazioni riguardo il cliente */

            String checkoutInfo = request.getParameter("checkoutInfo");

            System.err.println("checkoutInfo ===> " + checkoutInfo);
            JSONObject mainObj = new JSONObject(checkoutInfo);
            /* prezzo totale al momento del checkout che assumiamo non venga variato anche se nel frattempo variano i prezzi */
            totalPrice = new BigDecimal((String) mainObj.get("totalPrice"));
            totalSaved = new BigDecimal((String) mainObj.get("totalSaved"));

            /* array di prodotti da acquistare del tipo ==> "productsToBuy":[{"ID":"1","desiredQty":"1"},{"ID":"4","desiredQty":"4"},{"ID":"5","desiredQty":"2"}] */

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
                checkoutProducts.add(productToShow);
            }
            System.out.println("totalPrice:" + totalPrice + " | totalSaved:" + totalSaved);

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

            productDAO = daoFactory.getProductDAO();

            ordersDAO = daoFactory.getOrdersDAO();

            userDAO = daoFactory.getUserDAO();

            cartDAO = daoFactory.getCartDAO();

            customer = userDAO.findById(loggedUser.getId());

            /* fetch dei parametri passati dalla pagina checkout.jsp */
            BigDecimal totalPrice = new BigDecimal(request.getParameter("totalPrice"));
            String[] ids = request.getParameterValues("ids");
            String[] quantities = request.getParameterValues("quantities");


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


            /* Effettuo l'inserimento del nuovo prodotto nella tabella degli ordini */

            ordersDAO.insert(customer, totalPrice, items);
            inserted = true; /* Se non viene sollevata l'eccezione, è stato inserito correttamente*/

            if(inserted){
                /* se gli ordini sono andati a buon fine devo rimuovere i prodotti dal carrello dell'utente */
                for (ExtendedProduct boughtItem : items){
                    cartDAO.removeProductFromCart(loggedUser,boughtItem.getId());
                }
                applicationMessage = "Order received correctly, check the status in your order section.";
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


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/result-checkout");
        /* 4) il messaggio da visualizzare */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (inserted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
//        /* 4) Lista dei prodotti da mostrare nel riepilogo del checkout */
//        request.setAttribute("checkoutProducts",checkoutProducts);
        /* 5) Prezzo totale da pagare */
//        request.setAttribute("totalPrice",totalPrice);
//        /* 6) Totale risparmiato grazie agli sconti */
//        request.setAttribute("totalSaved",totalSaved);
//        /* 7) Oggetto utente per sapere ulteriori informazioni riguardo il cliente */
//        request.setAttribute("customer",customer);

    }
}
