package admin.controller;

import model.dao.DAOFactory;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Bookings {


    public static void showBookings(HttpServletRequest request, HttpServletResponse response) {
        /*
         * Instantiates an CustomerDAO to be able to show ALL customers in Database.
         */
        DAOFactory daoFactory = null;

        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Bookings.showBookings ==> daoFactory.beginTransaction();");
        }

/*        commonView(daoFactory,request);

        *//* Effettuo le ultime operazioni di commit e poi successiva chiusura della transazione *//*
        try {
            daoFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT DELLA TRANSAZIONE");
        } finally {
            *//* Chiudo la transazione*//*
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }*/


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-bookings");
    }

/*    private static void commonView(DAOFactory daoFactory, HttpServletRequest request){
        CustomerDAO customerDAO = null;
        ArrayList<Customer> customers = null;

        customerDAO = daoFactory.getCustomerDAO();

        *//* Scarico dal DB l'intera lista dei clienti *//*
        customers = customerDAO.fetchAll();

        request.setAttribute("customers", customers);
    }*/








}
