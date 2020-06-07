package home.controller;

import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.ExtendedProduct;
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
                BigDecimal eachPrice = new BigDecimal((String) productObj.get("eachPrice"));

                System.out.println("ID=" + ID + " | desiredQty=" + desiredQty + " | name=" + name + " | eachPrice=" + eachPrice);

                ExtendedProduct productToShow = new ExtendedProduct();
                productToShow.setId(ID);
                productToShow.setRequiredQuantity(desiredQty);
                productToShow.setName(name);
                productToShow.setPrice(eachPrice);
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
        request.setAttribute("checkoutProducts",checkoutProducts);
        /* 5) Prezzo totale da pagare */
        request.setAttribute("totalPrice",totalPrice);
        /* 6) Totale risparmiato grazie agli sconti */
        request.setAttribute("totalSaved",totalSaved);

    }
}
