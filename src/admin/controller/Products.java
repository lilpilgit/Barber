package admin.controller;

import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Products {

    private Products() {

    }

    public static void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates a ProductDAO to be able to enter the new product in Database.
         */
        String producer = null;
        BigDecimal price = null;
        Integer discount = null;
        String name = null;
        LocalDate insertDate = null;
        String description = null;
        Integer maxOrderQuantity = null;
        String category = null;
        String submit; /*mi aspetto che il value sia "add_new_product"*/
        String pictureName = null; /* nome del file da calcolare a runtime */


        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        ProductDAO productDAO = null;
        Product productInserted = null; /* serve per conoscere l'id del prodotto appena aggiunto */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento */
        Structure structure = null;
        FileItem imgField = null;
        String fileExtension = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Dato che è previsto il caricamento dell'immagine, prendo l'attributo attributesMultipart */
            List<FileItem> items = (List<FileItem>) request.getAttribute("attributesMultipart");

            String imagePath = request.getServletContext().getRealPath(Configuration.PRODUCTS_IMAGE_WEB_PATH);
            /*   http://localhost:8080/ INTERNET  ==> LOCALE  { /home/user/----/img/products } questo è imagePath */

            /* Processo i field del form inviatomi */
            Iterator<FileItem> iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (item.isFormField()) {
                    /* se è un campo della form NON BINARIO lo inserisco all'interno dell'HashMap<String,String>*/
                    String fieldName = item.getFieldName();

                    /* Fetching dei parametri provenienti dal form di inserimento e salvataggio nelle variabili locali */
                    if (fieldName.equals("producer")) {
                        producer = item.getString();/*required*/
                    } else if (fieldName.equals("name")) {
                        name = item.getString();/*required*/
                    } else if (fieldName.equals("category")) {
                        category = item.getString();/*required*/
                    } else if (fieldName.equals("description")) {
                        description = item.getString();/*required*/
                    } else if (fieldName.equals("price")) {
                        price = BigDecimal.valueOf(Double.parseDouble(item.getString()));/*required*/
                    } else if (fieldName.equals("discount")) {
                        discount = Integer.parseInt(item.getString());/*required*/
                    } else if (fieldName.equals("maxOrderQuantity")) {
                        maxOrderQuantity = Integer.parseInt(item.getString());/*required*/
                    } else if (fieldName.equals("insert_date")) {
                        insertDate = LocalDate.parse(item.getString());/*not required*/
                    } else if (fieldName.equals("submit")) {
                        submit = item.getString();/*required*/
                    }

                } else {
                    /* altrimenti non si tratta di un campo della form ma di un file binario */
                    /* Dato che salverò il file immagine nel filesystem con il nome con tale pattern : product_<id_del_prodotto>
                     * (così da poterlo sovrascrivere nel momento in cui verrà scelta una nuova immagine), mi salvo l'item
                     * all'interno di un FileItem (imgField) così da aggiungere il file solo se l'inserimento sul DB è andato
                     * a buon fine.*/
                    imgField = item; /*required*/
                    fileExtension = FilenameUtils.getExtension(imgField.getName()); /* recupero l'estensione del file */
                }
            }

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

            productDAO = daoFactory.getProductDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Scarico dal DB l'UNICA struttura  */
            structure = structureDAO.fetchStructure();

            /* Effettuo l'inserimento del nuovo prodotto */
            try {
                productInserted = productDAO.insert(null, producer, price, discount, name, insertDate, Configuration.BASE_PIC_NAME, fileExtension, description, maxOrderQuantity, category, structure);
                inserted = true; /* Se non viene sollevata l'eccezione, è stato inserito correttamente*/
            } catch (DuplicatedObjectException e) {
                System.err.println("CHIAMATO NELLA PRODUCTS");
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            System.err.println("DOPO IL CATCH DI DUPLICATED");
            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (inserted) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto sia stato inserito correttamente .*/
                applicationMessage = "Product inserted SUCCESSFULLY.";

                /* Procedo al salvataggio dell'immagine sul disco */

                pictureName = Configuration.BASE_PIC_NAME + productInserted.getId() + "." + fileExtension; /* product_100.jpg */
                String uploadedFilePath = imagePath + "/" + pictureName;
                File uploadedFile = new File(uploadedFilePath);
                imgField.write(uploadedFile);/* scrivo sul file appena creato il contenuto binario del file passato dalla form */
            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il prodotto non è stato inserito .*/
                applicationMessage = "Product insertion ERROR.";

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
         *     form per consentire un nuovo inserimento */
        request.setAttribute("viewUrl", "admin/new-edit-product");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (inserted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        request.setAttribute("structure", structure);

    }

    public static void showProducts(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an ProductsDAO to be able to show ALL Products in Database.
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;

        try {
            sessionDAOFactory = initializeCookie(request, response);


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
        boolean status = false; /* in base allo stato capisco se sto chiedendo di mettere o rimuovere un prodotto dalla vetrina */
        boolean fromHome = false; /* If the request comes from the home, I known! */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare il manage */
        Product product = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean showcase = false; /* Showcase indica se il prodotto e' o no in vetrina nella home */

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Fetching dell'id da bloccare proveniente dal form hidden dentro la pagina show-product.jsp */
            idToManage = Long.valueOf(request.getParameter("ProductID"));
            /* Fetching dello stato attuale del prodotto, se e' in vetrina o no */
            status = Boolean.parseBoolean(request.getParameter("ProductStatus"));
            /* Fetching della provenienza della richiesta, se viene dalla home --> true */
            fromHome = Boolean.parseBoolean(request.getParameter("fromHome"));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            productDAO = daoFactory.getProductDAO();
            product = productDAO.findProductById(idToManage);

            /* Negando status, quando chiamo la funzione modifyShowcase settero' il valore inverso a quello presente nel db */
            showcase = productDAO.modifyShowcase(product, !status); /* Se non viene sollevata l'eccezione è stato modificato correttamente*/

            commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (showcase) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che sia stato aggiornato correttamente .*/
                applicationMessage = "The product was " + ((status) ? "removed from" : "added to") + " the showcase SUCCESSFULLY!";
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
        /* 4) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (showcase) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 5) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-customers.jsp per consentire altre operazioni */
        if (fromHome) {
            /* se la richiesta di rimozione del prodotto dalla vetrina viene dalla Home, allora chiamo la view */
            home.controller.Home.view(request, response);
        } else {
            request.setAttribute("viewUrl", "admin/show-products");
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
            sessionDAOFactory = initializeCookie(request, response);

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
            sessionDAOFactory = initializeCookie(request, response);

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
        /* Setto gli attributi della request che verranno processati dalla new-edit-product.jsp */
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
        String productId = null;
        String producer = null;
        BigDecimal price = null;
        Integer discount = null;
        String name = null;
        LocalDate insertDate = null;
        String description = null;
        Integer maxOrderQuantity = null;
        String category = null;
        String submit; /*mi aspetto che il value sia "edit_product"*/
        String basePicName = "product_"; /* file di name che funge come base */
        String picture_name_stored = null; /* il nome attuale del file immagine */
        Boolean inShowcase = null; /* per sapere se il prodotto da modificare è nello showcase o meno */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare la modifica */
        Product productToEdit = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare la modifica */
        Structure structure = null;
        Product originalProduct = null; /* l'oggetto intatto ancora con i campi non modificati */
        FileItem imgField = null;
        String fileExtension = null;
        boolean changeImage = false;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;

//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_product"*/

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Dato che è previsto il caricamento dell'immagine, prendo l'attributo attributesMultipart */
            List<FileItem> items = (List<FileItem>) request.getAttribute("attributesMultipart");

            String imagePath = request.getServletContext().getRealPath(Configuration.PRODUCTS_IMAGE_WEB_PATH);

            /* Processo i field del form inviatomi */
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (item.isFormField()) {
                    /* se è un campo della form NON BINARIO lo inserisco all'interno dell'HashMap<String,String>*/
                    String fieldName = item.getFieldName();

                    /* Fetching dei parametri provenienti dal form di inserimento e salvataggio nelle variabili locali */
                    if (fieldName.equals("producer")) {
                        producer = item.getString();/*required*/
                    } else if (fieldName.equals("productId")) {
                        productId = item.getString(); /* hidden */
                    } else if (fieldName.equals("picture_name_stored")) {
                        picture_name_stored = item.getString();
                    } else if (fieldName.equals("name")) {
                        name = item.getString();/*required*/
                    } else if (fieldName.equals("category")) {
                        category = item.getString();/*required*/
                    } else if (fieldName.equals("description")) {
                        description = item.getString();/*required*/
                    } else if (fieldName.equals("price")) {
                        price = BigDecimal.valueOf(Double.parseDouble(item.getString()));/*required*/
                    } else if (fieldName.equals("discount")) {
                        discount = Integer.parseInt(item.getString());/*required*/
                    } else if (fieldName.equals("maxOrderQuantity")) {
                        maxOrderQuantity = Integer.parseInt(item.getString());/*required*/
                    } else if (fieldName.equals("insert_date")) {
                        insertDate = LocalDate.parse(item.getString());/*not required*/
                    } else if (fieldName.equals("showcase")) {
                        inShowcase = Boolean.parseBoolean(item.getString());
                    } else if (fieldName.equals("submit")) {
                        submit = item.getString();/*required*/
                    }

                } else {
                    if (item.getSize() != 0) {
                        System.out.println("DIMENSIONE INVIATA " + item.getSize());
                        /* È STATO PASSATO ANCHE UN FILE DUNQUE SIGNIFICA CHE VA SOVRASCRITTO */
                        /* altrimenti non si tratta di un campo della form ma di un file binario */
                        /* Dato che salverò il file immagine nel filesystem con il nome con tale pattern : product_<id_del_prodotto>
                         * (così da poterlo sovrascrivere nel momento in cui verrà scelta una nuova immagine), mi salvo l'item
                         * all'interno di un FileItem (imgField) così da aggiungere il file solo se l'inserimento sul DB è andato
                         * a buon fine.*/
                        changeImage = true;
                        imgField = item; /*required*/
                        fileExtension = FilenameUtils.getExtension(imgField.getName()); /* recupero l'estensione del file */
                    } else {
                        changeImage = false;
                        System.out.println("NON SI VUOLE MODIFICARE IL FILE IMMAGINE!");
                    }
                }
            }


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

            productDAO = daoFactory.getProductDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo update() su productDAO ) */
            structure = structureDAO.fetchStructure();

            /* In caso di modifica, il form contiene un campo hidden con name="productId" che mi viene
             * passato dalla JSP e consente di poter scaricare dal DB l'impiegato con quel determinato ID */
            originalProduct = productDAO.findProductById(Long.valueOf(productId));

            /* Li tratto come oggetti separati così da poter decidere alla fine, in base all'esito dell'update
             * quale passare alla pagina new-edit-product.jsp */
            productToEdit = productDAO.findProductById(Long.valueOf(productId));

            /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
             * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

            /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
             * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
             * */

            productToEdit.setName(name);
            productToEdit.setProducer(producer);
            productToEdit.setCategory(category);
            productToEdit.setDescription(description);
            productToEdit.setPictureName((changeImage) ? basePicName + productId + "." + fileExtension : picture_name_stored);
            productToEdit.setInsertDate(insertDate);
            productToEdit.setPrice(price);
            productToEdit.setDiscount(discount);
            productToEdit.setMaxOrderQuantity(maxOrderQuantity);
            productToEdit.setDeleted(false);
            productToEdit.setShowcase(inShowcase);
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
                if (changeImage) {
                    System.out.println("SI VUOLE MODIFICARE IL FILE IMMAGINE!");
                    /* E' stata passata anche una nuova immagine, devo cancellare quella vecchia
                     * ( per permettere a inotify di registrare tale evento e cancellarmi il file
                     *  anche dalla cartella img/products di sviluppo) e scrivere il nuovo file con lo stesso nome */
                    String oldFileUploadedPath = imagePath + "/" + picture_name_stored; /* stesso nome */
                    System.out.println("oldFileUploadedPath ==> " + oldFileUploadedPath);
                    File oldFileUploaded = new File(oldFileUploadedPath); /* vecchio file*/

                    /* cancello il vecchio file */
                    if (oldFileUploaded.delete()) {
                        System.err.println("VECCHIO FILE CANCELLATO CORRETTAMENTE");
                    }

                    /* file cancellato con successo, scrivo il nuovo file */
                    String newFileUploadedPath = imagePath + "/" + basePicName + productId + "." + fileExtension; /* stesso nome */
                    System.out.println("newFileUploadedPath ==> " + newFileUploadedPath);
                    File newUploadedFile = new File(newFileUploadedPath);
                    imgField.write(newUploadedFile); /* scrivo sul file appena creato il contenuto binario del file passato dalla form */

                }
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
                applicationMessage = "Product modification ERROR.";
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
        request.setAttribute("viewUrl", "admin/new-edit-product");
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

    public static void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates an ProductDAO to be able to delete product from Database.
         */
        Long idToDelete = null; /* id ricevuto da cancellare*/
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        DAOFactory daoFactory = null;
        ProductDAO productDAO = null; /* DAO Necessario per poter effettuare la cancellazione */
        Product product = null;
        String pictureName;
        String imagePath = request.getServletContext().getRealPath(Configuration.PRODUCTS_IMAGE_WEB_PATH);
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* Fetching dell'id da cancellare proveniente dal form hidden dentro la pagina show-product.jsp */
            idToDelete = Long.valueOf(request.getParameter("ProductID"));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* è necessario prendersi solo lo userDAO per poter settare su uno specifico utente il flag DELETED */
            productDAO = daoFactory.getProductDAO();

            /* trovo il prodotto da flaggare come cancellato */
            product = productDAO.findProductById(idToDelete);

            deleted = productDAO.delete(product);

            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Se il prodotto è stato cancellato committo la transazione */
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

            if (deleted) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il prodotto sia stato cancellato correttamente .*/
                applicationMessage = "Product deleted SUCCESSFULLY.";

                /* Procedo all'eliminazione dell'immagine sul disco */

                pictureName = product.getPictureName(); /* prendo il nome della foto del prodotto da eliminare */
                String toDeleteFilePath = imagePath + "/" + pictureName;
                File toDeleteFile = new File(toDeleteFilePath);
                if(toDeleteFile.delete()){
                    System.out.println("File immagine {" + pictureName + " del prodotto con ID {" + product.getId() + "} cancellato correttamente.");
                }else{
                    System.err.println("ERRORE durante la cancellazione del file immagine {" + pictureName + " per il prodotto con ID {" + product.getId() + "}.");
                }
            }

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione non è stato cancellato .*/
                applicationMessage = "Product cancellation ERROR.";

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

        /* Setto gli attributi della request che verranno processati dalla show-customer.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-products.jsp per consentire altre cancellazioni */
        request.setAttribute("viewUrl", "admin/show-products");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (deleted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }

    }

    public static void searchProducts(HttpServletRequest request, HttpServletResponse response) {

        /**
         * Instantiates an ProductsDAO to be able to show filtered Products in Database on a search string.
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        String searchString = null;
        ProductDAO productDAO = null;
        ArrayList<Product> foundProducts = null;

        try {
            sessionDAOFactory = initializeCookie(request, response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            searchString = request.getParameter("searchString");

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            productDAO = daoFactory.getProductDAO();

            foundProducts = productDAO.findProductsByString(searchString);

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
        /* 3) Quale jsp mostrare */
        request.setAttribute("viewUrl", "admin/show-products");
        /* 4) Stringa usata nella ricerca da ri-mostrare all'utente */
        request.setAttribute("searchedString",searchString);
        /* 5) Lista dei prodotti trovati sulla base della query string */
        request.setAttribute("products", foundProducts);

    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        ProductDAO productDAO = null;
        ArrayList<Product> products = null;

        productDAO = daoFactory.getProductDAO();

        /* Scarico dal DB l'intera lista dei prodotti */
        products = productDAO.fetchAllProducts();

        request.setAttribute("products", products);
    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }

}
