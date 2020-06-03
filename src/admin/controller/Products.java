package admin.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class Products {

    private Products() {

    }

    public static void showProducts(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an ProductsDAO to be able to show ALL Products in Database.
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
            }
        }

        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-products");

    }

    public static void manageShowcase(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an ProductDAO to be able to set/unset showcase from Database.
         */
        Long idToManage = null; /* id ricevuto */
        Boolean status = false; /* in base allo stato capisco se sto chiedendo di mettere o rimuovere un prodotto dalla vetrina */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare il manage */
        Product product = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean showcase = false; /* Showcase indica se il prodotto e' o no in vetrina nella home */

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Fetching dell'id da bloccare proveniente dal form hidden dentro la pagina show-product.jsp */
            idToManage = Long.valueOf(request.getParameter("ProductID"));
            /* Fetching dello stato attuale del prodotto, se e' in vetrina o no */
            status = Boolean.valueOf(request.getParameter("ProductStatus"));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            productDAO = daoFactory.getProductDAO();
            product = productDAO.findProductById(idToManage);

            /* Negando status, quando chiamo la funzione modifyShowcase settero' il valore inverso a quello presente nel db */
            showcase = productDAO.modifyShowcase(product, !status); /* Se non viene sollevata l'eccezione è stato modificato correttamente*/

            /* Chiamo la commonView in modo da far vedere il risultato aggiornato che si presentera' come un cambiamento
            a livello grafico (cambio colore scritte o icone) */

            commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            if (showcase) {
                /* Se tutto va a buon fine, committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Solo se viene committata la transazione senza errori siamo sicuri che sia stato aggiornato correttamente .*/
                applicationMessage = "SUCCESS!";

            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione non è stato settato in vetrina .*/
                applicationMessage = "manageShowcase ERROR :(";
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
            }
        }

        /* Setto gli attributi della request che verranno processati dalla show-product.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-customers.jsp per consentire altre operazioni */
        request.setAttribute("viewUrl", "admin/show-products");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (showcase) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

    }

    public static void showFormNewProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set viewUrl to the JSP <new-edit-product> to show the data entry form of the existent product.
         * Send product to modify as attribute of the request, to fill in edit-product.jsp automatically
         * field with data of existent product.
         * */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        StructureDAO structureDAO = null;
        Structure structure = null;


        String applicationMessage = null; /* messaggio da mostrare a livello applicativo ritornato dai DAO */

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Nel caso in cui ci fossero piu' strutture */
            structureDAO = daoFactory.getStructureDAO();
            structure = structureDAO.fetchStructure();

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato inserito .*/
                applicationMessage = "An error occurred.";

            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
                applicationMessage = "An error occurred.";
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
                applicationMessage = "An error occurred.";
            }
        }
        /* Setto gli attributi della request che verranno processati dalla new-edit-product.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 4) il viewUrl che il dispatcher dovrà visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/new-edit-product"); /* bisogna visualizzare new-edit-product.jsp */
        /* 5) i dati della struttura sulla quale si sta operando da mostrare nella pagina edit-product.jsp */
        request.setAttribute("structure", structure);
        /* 5) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);

    }

    public static void showFormEditProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set viewUrl to the JSP <edit-product> to show the data entry form of the existent product.
         * Send product to modify as attribute of the request, to fill in edit-product.jsp automatically
         * field with data of existent product.
         * */
        Long idToEdit = null; /* Id da modificare */
        Product productToEdit = null; /* oggetto che deve essere passato alla pagina del form di inserimento/modifica */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        StructureDAO structureDAO = null;
        Structure structure = null;
        ProductDAO productDAO = null;
        String applicationMessage = null; /* messaggio da mostrare a livello applicativo ritornato dai DAO */

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Fetching dei parametri provenienti dal form di modifica e salvataggio nelle variabili locali */
            idToEdit = Long.valueOf(request.getParameter("ProductID"));


            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Nel caso in cui ci fossero piu' strutture */
            structureDAO = daoFactory.getStructureDAO();
            structure = structureDAO.fetchStructure();

            productDAO = daoFactory.getProductDAO();
            productToEdit = productDAO.findProductById(idToEdit);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato inserito .*/
                applicationMessage = "An error occurred.";

            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
                applicationMessage = "An error occurred.";
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
                applicationMessage = "An error occurred.";
            }
        }
        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) il viewUrl che il dispatcher dovrà visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/new-edit-product"); /* bisogna visualizzare new-edit-product.jsp */
        /* 5) i dati della struttura sulla quale si sta operando da mostrare nella pagina new-edit-product.jsp */
        request.setAttribute("structure", structure);
        /* 6) l'oggetto impiegato che deve essere modificato */
        request.setAttribute("productToModify", productToEdit);

    }

    public static void editProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a ProductDAO to be able to edit the existing product in Database.
         */

        String submit; /*mi aspetto che il value sia "edit_product"*/

        DAOFactory daoFactory = null; //per il db
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare la modifica */
        Product productToEdit = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare la modifica */
        Structure structure = null;
        Product originalProduct = null; /* l'oggetto intatto ancora con i campi non modificati */
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;

//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_product"*/

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Fetching dei parametri provenienti dal form di inserimento e salvataggio nelle variabili locali */

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            productDAO = daoFactory.getProductDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo update() su productDAO ) */
            structure = structureDAO.fetchStructure();
            System.err.println(structure);

            /* In caso di modifica, il form contiene un campo hidden con name="productId" che mi viene
             * passato dalla JSP e consente di poter scaricare dal DB l'impiegato con quel determinato ID */
            originalProduct = productDAO.findProductById(Long.valueOf(request.getParameter("productId")));

            /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
             * quale passare alla pagina new-edit-product.jsp */
            productToEdit = productDAO.findProductById(Long.valueOf(request.getParameter("productId")));

            /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
             * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

            /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
             * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
             * */

            productToEdit.setName(request.getParameter("name"));
            productToEdit.setProducer(request.getParameter("producer"));
            productToEdit.setCategory(request.getParameter("category"));
            productToEdit.setDescription(request.getParameter("description"));
            productToEdit.setPictureName(request.getParameter("pic_name"));
            productToEdit.setInsertDate(LocalDate.parse(request.getParameter("insert_date")));
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(request.getParameter("price")));
            productToEdit.setPrice(price);
            productToEdit.setDiscount(Integer.parseInt(request.getParameter("discount")));
            productToEdit.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            productToEdit.setDeleted(false);
            productToEdit.setShowcase(true);
            productToEdit.setStructure(structure);


            /* Effettuo la modifica */
            try {
                edited = productDAO.update(productToEdit);/* Se non viene sollevata l'eccezione, è stato modificato correttamente*/

            } catch (DuplicatedObjectException e) {
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            if (edited) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che sia stato modificato correttamente .*/
                applicationMessage = "Product modified SUCCESSFULLY.";
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione non è stato modificato .*/
                applicationMessage = "Employee modification ERROR.";
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
            }
        }
        /* Setto gli attributi della request che verranno processati dalla new-edit-product.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo impiegato */
        request.setAttribute("viewUrl", "admin/new-edit-employee");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 6) il prodotto che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("productToModify", productToEdit);
        } else {
            /* FAIL */
            request.setAttribute("employeeToModify", originalProduct);
        }
        /* 7) l'UNICA struttura da mostrare readonly */
        request.setAttribute("structure", structure);
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        ProductDAO productDAO = null;
        ArrayList<Product> products = null;

        productDAO = daoFactory.getProductDAO();

        /* Scarico dal DB l'intera lista dei clienti */
        products = productDAO.fetchAllProducts();

        request.setAttribute("products", products);
    }

}
