package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Structures {

    private Structures() {

    }

    public static void showStructures(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an StructuresDAO to be able to show ALL Structures in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Structures.showStructures ==> daoFactory.beginTransaction();");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-structures");

    }
}

