package admin.controller;

import model.dao.CustomerDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.mo.Customer;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

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

        request.setAttribute("viewUrl", "admin/show-customers");
    }

    public static void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates an CustomerDAO to be able to delete customer from Database.
         */
        Long idToDelete = null; /* id del cliente ricevuto da cancellare*/

        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare la cancellazione del cliente */
        User user = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;

        /* Fetching dell'id dell'impiegato da cancellare proveniente dal form hidden dentro la pagina show-employees.jsp */
        idToDelete = Long.valueOf(request.getParameter("CustomerID"));

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.deleteEmployee ==> daoFactory.beginTransaction();");
        }

        /* è necessario prendersi solo lo userDAO per poter settare su uno specifico utente il flag DELETED */
        userDAO = daoFactory.getUserDAO();

        /* trovo l'utente da flaggare come cancellato */
        user = userDAO.findById(idToDelete);

        /* Effettuo la cancellazione del cliente usando il metodo delete dello UserDAO in quanto il flag DELETED
         * è presente solamente nella tabella USER */

        deleted = userDAO.delete(user); /* Se non viene sollevata l'eccezione, l'impiegato è stato cancellato correttamente*/

        /* Chiamo la commonView che si occuperà di ricaricare la lista dei clienti aggiornata, togliendo quelli eliminati
         *  e scaricare le informazioni riguardo la struttura che stiamo gestendo. È necessario chiamarla in quanto la cancellazione,
         * a differenza dell'aggiunta non ha una propria pagina, ma consiste nel click di un semplice bottone ( il cestino ) pertanto è
         * necessario ricaricare la lista dei dipendenti aggiornata ( chiamando appunto la commonView ) e solo settare ( come faccio sotto )
         * la viewUrl alla pagina show-customer.jsp */
        commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (deleted) {
                /* Se il cliente è stato inserito cancellato committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che il cliente sia stato cancellato correttamente .*/
                applicationMessage = "Customer deleted SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione il cliente non è stato cancellato .*/
                applicationMessage = "Customer cancellation ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }


        /* Setto gli attributi della request che verranno processati dalla show-customer.jsp */

        /* 1) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-employee.jsp per consentire altre cancellazioni */
        request.setAttribute("viewUrl", "admin/show-customers");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (deleted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

        System.err.println("Chiamato con successo");

        System.err.println("Mi aspetto ==> " + request.getParameter("CustomerID"));
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request){
        CustomerDAO customerDAO = null;
        ArrayList<Customer> customers = null;

        customerDAO = daoFactory.getCustomerDAO();

        /* Scarico dal DB l'intera lista dei clienti */
        customers = customerDAO.fetchAll();

        request.setAttribute("customers", customers);
    }

}