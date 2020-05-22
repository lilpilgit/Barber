package admin.controller;

import common.StaticFunc;
import model.dao.*;
import model.exception.DuplicatedObjectException;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public class Admin {

    private Admin() {
    }

    public static void showFormEditAdmin(HttpServletRequest request, HttpServletResponse response){
        /**
         * SI OCCUPA DI RICHIAMARE LA JSP IN CUI È CONTENUTO IL FORM PER LA MODIFICA DEI PROPRI DATI
         * */

        /* TODO:CODICE DA RIVEDERE DOPO CHE SARANNO STATE INTRODOTTE LE SESSIONI*/
        /*TODO prendere l'ID dell'admin da modificare dalla SESSIONE*/

        model.mo.Admin adminToEdit = null; /* oggetto di classe Admin che deve essere passato alla pagina del form di inserimento/modifica */

        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Admin.showFormEditAdmin ==> daoFactory.beginTransaction();");
        }

        AdminDAO adminDAO = daoFactory.getAdminDAO();
        adminToEdit = adminDAO.findById(1L);

        try {
            /* committo la transazione */
            daoFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT DELLA TRANSAZIONE");
        } finally {
            /*  chiudo la transazione */
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request */
        /* 1) il viewUrl che il dispatcher dovrà visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/edit-admin"); /* bisogna visualizzare edit-admin.jsp */
        /* 2) l'oggetto impiegato che deve essere modificato */
        request.setAttribute("adminToModify",adminToEdit);
    }

    public static void editAdmin(HttpServletRequest request, HttpServletResponse response){
        /**
         * Instantiates an AdminDAO to be able to edit the existing admin in Database.
         */

//        String submit; /*mi aspetto che il value sia "edit_admin"*/

        DAOFactory daoFactory = null;
        AdminDAO adminDAO = null;
        model.mo.Admin adminToEdit = null;
        model.mo.Admin originalAdmin = null; /* l'oggetto intatto ancora con i campi non modificati */

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;


//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_admin"*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Admin.editAdmin ==> daoFactory.beginTransaction();");
        }

        adminDAO = daoFactory.getAdminDAO();


        /* In caso di modifica, il form contiene un campo hidden con name="adminId" che mi viene
         * passato dalla JSP e consente di poter scaricare dal DB l'admin con quel determinato ID */
        originalAdmin = adminDAO.findById(Long.valueOf(request.getParameter("adminId")));

        /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
         * quale passare alla pagina edit-admin.jsp */
        adminToEdit = adminDAO.findById(Long.valueOf(request.getParameter("adminId")));

        /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
         * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

        /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
         * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
         * */
        adminToEdit.getUser().setName(request.getParameter("name")); /* attributo della tabella USER */
        adminToEdit.getUser().setSurname(request.getParameter("surname")); /* attributo della tabella USER */
        adminToEdit.getUser().setEmail(request.getParameter("email")); /* attributo della tabella USER */
        adminToEdit.getUser().setPhone(request.getParameter("phone")); /* attributo della tabella USER */
        adminToEdit.getUser().setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number"))); /* attributo della tabella USER */
        adminToEdit.getUser().setIsAdmin(true); /* attributo della tabella USER */
        adminToEdit.getUser().setIsEmployee(false); /* attributo della tabella USER */
        adminToEdit.getUser().setIsCustomer(false); /* attributo della tabella USER */
        adminToEdit.getUser().setIsDeleted(false); /* attributo della tabella USER */
        adminToEdit.setBirthDate(LocalDate.parse(request.getParameter("birth_date"))); /* attributo della tabella ADMIN */
        adminToEdit.setFiscalCode(request.getParameter("fiscal_code")); /* attributo della tabella ADMIN */

        /* Effettuo la modifica dell'admin */
        try {
            edited = adminDAO.update(adminToEdit);/* Se non viene sollevata l'eccezione, l'admin è stato modificato correttamente*/

        } catch (DuplicatedObjectException e) {
            applicationMessage = e.getMessage();
            e.printStackTrace();
        }

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (edited) {
                /* Se l'admin è stato modificato correttamente committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che l' admin sia stato modificato correttamente .*/
                applicationMessage = "Admin modified SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione l'admin non è stato modificato .*/
                applicationMessage = "Admin modification ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla edit-admin.jsp */

        /* 1) il messaggio da visualizzare nella pagina di modifica solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo admin */
        request.setAttribute("viewUrl", "admin/edit-admin");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) l'admin che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("adminToModify", adminToEdit);
        } else {
            /* FAIL */
            request.setAttribute("adminToModify", originalAdmin);
        }

    }




}
