package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Customers {

    private Customers() {

    }

    public static void manageCustomer(HttpServletRequest request, HttpServletResponse response) {
        /* SI OCCUPA DI RICHIAMARE LA JSP IN CUI E' CONTENUTO IL FORM PER VISUALIZZARE TUTTI I CLIENTI RIFERITI AD UNA
           CERTA STRUTTURA CHE HANNO ACQUISTI O PRENOTAZIONI IN CORSO
         */

        request.setAttribute("viewUrl", "admin/manage-customer");
    }


}
