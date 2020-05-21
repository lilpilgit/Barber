package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Welcome {

    private Welcome() {
    }

    public static void welcome(HttpServletRequest request, HttpServletResponse response) {
        /**
         * SI OCCUPA DI RICHIAMARE LA JSP IN CUI È CONTENUTO IL MESSAGGIO DI BENVENUTO DELL'ADMIN TODO:O ALTRE COSE
         * DA MOSTRARE IN FUTURO
         * */
        request.setAttribute("viewUrl", "admin/view");
        /*TODO parametri di sessione da passare se loggato o meno*/
    }
}
