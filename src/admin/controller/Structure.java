package admin.controller;

import common.StaticFunc;
import model.dao.DAOFactory;
import model.dao.StructureDAO;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;


public class Structure {

    private Structure() {

    }

    public static void showStructure(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an StructuresDAO to be able to show Structure in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Structure.showStructure ==> daoFactory.beginTransaction();");
        }

        commonView(daoFactory, request);

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


        /* Setto gli attributi della request che verranno processati dalla edit-structure.jsp */

        request.setAttribute("viewUrl", "admin/edit-structure");

    }

    public static void editStructure(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an StructureDAO to be able to edit the existing structure in Database.
         */

//        String submit; /*mi aspetto che il value sia "edit_structure"*/

        DAOFactory daoFactory = null;
        StructureDAO structureDAO = null;
        model.mo.Structure structureToEdit = null;
        model.mo.Structure originalStructure = null; /* l'oggetto intatto ancora con i campi non modificati */

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;


//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_structure"*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Structure.editStructure ==> daoFactory.beginTransaction();");
        }

        structureDAO = daoFactory.getStructureDAO();

        originalStructure = structureDAO.fetchStructure();

        /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
         * quale passare alla pagina edit-structure.jsp */
        structureToEdit = structureDAO.fetchStructure();

        /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
         * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

        /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
         * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
         * */
        structureToEdit.setName(request.getParameter("name"));
        structureToEdit.setPhone(request.getParameter("phone"));
        structureToEdit.setOpeningTime(request.getParameter("opening_time"));
        structureToEdit.setClosingTime(request.getParameter("closing_time"));
        structureToEdit.setSlot(request.getParameter("slot"));
        structureToEdit.setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number")));

        /* Effettuo la modifica della struttura */
        edited = structureDAO.update(structureToEdit);/* Se non viene sollevata l'eccezione, la struttura è stato modificata correttamente*/

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (edited) {
                /* Se la struttura è stata modificata correttamente committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che la struttura sia stata modificata correttamente .*/
                applicationMessage = "Structure modified SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione la struttura non è stata modificata .*/
                applicationMessage = "Structure modification ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla edit-structure.jsp */

        /* 1) il messaggio da visualizzare nella pagina di modifica solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo admin */
        request.setAttribute("viewUrl", "admin/edit-structure");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) la struttura che è stata modificata e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("structureToModify", structureToEdit);
        } else {
            /* FAIL */
            request.setAttribute("structureToModify", originalStructure);
        }
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        StructureDAO structureDAO = null;
        model.mo.Structure structure = null;

        structureDAO = daoFactory.getStructureDAO();

        /* Scarico dal DB la struttura */
        structure = structureDAO.fetchStructure();

        request.setAttribute("structureToModify", structure);
    }
}

