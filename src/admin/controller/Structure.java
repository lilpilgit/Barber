package admin.controller;

import model.dao.DAOFactory;
import model.dao.StructureDAO;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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


        /* Setto gli attributi della request che verranno processati dalla edit-structure.jsp */

        request.setAttribute("viewUrl", "admin/edit-structure");

    }

    public static void editStructure(HttpServletRequest request, HttpServletResponse response){

    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request){
        StructureDAO structureDAO = null;
        model.mo.Structure structure = null;

        structureDAO = daoFactory.getStructureDAO();

        /* Scarico dal DB la struttura */
        structure = structureDAO.fetchStructure();

        request.setAttribute("structureToModify", structure);
    }
}

