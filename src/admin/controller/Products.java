package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Products {

    private Products() {

    }

    public static void showProducts(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an ProductsDAO to be able to show ALL Products in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Products.showProducts ==> daoFactory.beginTransaction();");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-products");

    }
}
