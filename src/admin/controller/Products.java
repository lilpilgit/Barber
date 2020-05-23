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

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request){
        ProductDAO productDAO = null;
        ArrayList<Product> products = null;

        productDAO = daoFactory.getProductDAO();

        /* Scarico dal DB l'intera lista dei clienti */
        products = productDAO.fetchAllProducts();

        request.setAttribute("products", products);
    }

}
