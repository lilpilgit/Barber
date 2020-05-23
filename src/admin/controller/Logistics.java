package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Logistics {

    private Logistics() {

    }

    public static void showLogistics(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an LogisticsDAO to be able to show ALL Orders in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Logistics.showLogistics ==> daoFactory.beginTransaction();");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-logistics");

    }
}
