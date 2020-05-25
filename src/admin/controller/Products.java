package admin.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.mo.Product;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


public class Products {

    private Products() {

    }

    public static void showProducts(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an ProductsDAO to be able to show ALL Products in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Products.showProducts ==> daoFactory.beginTransaction();");
        }

        commonView(daoFactory,request);

        /* Effettuo le ultime operazioni di commit e poi successiva chiusura della transazione */
        try {
            daoFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT DELLA TRANSAZIONE");
        } finally {
            /* Chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-products");
    }

    public static void manageShowcase(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an ProductDAO to be able to set/unset showcase from Database.
         */
        Long idToManage = null; /* id ricevuto */
        Boolean status = false; /* in base allo stato capisco se sto chiedendo di mettere o rimuovere un prodotto dalla vetrina */

        DAOFactory daoFactory = null;
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare il manage */
        Product product = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean showcase = false; /* Showcase indica se il prodotto e' o no in vetrina nella home */

        /* Fetching dell'id da bloccare proveniente dal form hidden dentro la pagina show-product.jsp */
        idToManage = Long.valueOf(request.getParameter("ProductID"));
        /* Fetching dello stato attuale del prodotto, se e' in vetrina o no */
        status = Boolean.valueOf(request.getParameter("ProductStatus"));

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Products.manageShowcase ==> daoFactory.beginTransaction();");
        }

        productDAO = daoFactory.getProductDAO();
        product = productDAO.findProductById(idToManage);

        /* Negando status, quando chiamo la funzione modifyShowcase settero' il valore inverso a quello presente nel db */
        showcase = productDAO.modifyShowcase(product, !status); /* Se non viene sollevata l'eccezione è stato modificato correttamente*/

        /* Chiamo la commonView in modo da far vedere il risultato aggiornato che si presentera' come un cambiamento
        a livello grafico (cambio colore scritte o icone) */

        commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (showcase) {
                /* Se tutto va a buon fine, committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che sia stato aggiornato correttamente .*/
                applicationMessage = "SUCCESS!";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione non è stato bloccato .*/
                applicationMessage = "manageShowcase ERROR :(";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla show-product.jsp */

        /* 1) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-customers.jsp per consentire altre operazioni */
        request.setAttribute("viewUrl", "admin/show-products");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (showcase) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

        System.err.println("Chiamato con successo");
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request){
        ProductDAO productDAO = null;
        ArrayList<Product> products = null;

        productDAO = daoFactory.getProductDAO();

        /* Scarico dal DB l'intera lista dei clienti */
        products = productDAO.fetchAllProducts();

        request.setAttribute("products", products);
    }

}
