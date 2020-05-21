package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Customers {

    private Customers() {

    }

    public static void showCustomers(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an CustomerDAO to be able to show ALL customers in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Customers.showCustomers ==> daoFactory.beginTransaction();");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-customers");


    }
}