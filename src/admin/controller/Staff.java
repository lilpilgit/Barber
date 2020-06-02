package admin.controller;

import functions.StaticFunc;
import model.dao.DAOFactory;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class Staff {

    private Staff() {

    }

    public static void showFormNewEmployee(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set viewUrl to the JSP <new-edit-employee> to show the data entry form of the new employee.
         * */

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
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
                applicationMessage = "An error occurred!";

            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
                applicationMessage = "An error occurred!";
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");

            } catch (Throwable t) {
                applicationMessage = "An error occurred!";
            }
        }
        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Attributo che indica la jsp da visualizzare nel browser */
        request.setAttribute("viewUrl", "admin/new-edit-employee"); /* bisogna visualizzare new-edit-employee.jsp */
        /* 4) Attributo che indica la struttura da visualizzare nel form */
        request.setAttribute("structure", structure);
        /* 5) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
    }

    public static void addEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates an EmployeeDAO to be able to enter the new employee in Database.
         */
        String name;
        String surname;
        String email;
        String phone;
        LocalDate hire_date;
        LocalDate birth_date;
        String fiscal_code;
        String state;
        String region;
        String city;
        String street;
        String house_number;
        String cap;
        String submit; /*mi aspetto che il value sia "add_new_employee"*/
        String password; /*password generata nel controller e NON proveniente dal form*/

        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        UserDAO employeeDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        Structure structure = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;

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
            email = request.getParameter("email");/*required*/
            name = request.getParameter("name");/*required*/
            surname = request.getParameter("surname");/*required*/
            street = request.getParameter("street");/*required*/
            state = request.getParameter("state"); /*required*/
            region = request.getParameter("region");/*required*/
            city = request.getParameter("city");/*required*/
            cap = request.getParameter("cap");/*required*/
            house_number = request.getParameter("house_number");/*not required*/
            phone = request.getParameter("phone");/*required*/
            /*   CREAZIONE DELLA PASSWORD SUPER SICURA!!!  */
            password = "password";
            /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
             * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
             * */
            birth_date = LocalDate.parse(request.getParameter("birth_date"));
            fiscal_code = request.getParameter("fiscal_code");
            hire_date = LocalDate.parse(request.getParameter("hire_date"));
            submit = request.getParameter("submit"); /*mi aspetto che il value sia "add_new_employee"*/


            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            employeeDAO = daoFactory.getUserDAO();

            structureDAO = daoFactory.getStructureDAO();

            /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo insert() su employeeDAO ) */
            structure = structureDAO.fetchStructure();
            System.err.println(structure);
            /* Effettuo l'inserimento del nuovo dipendente */
            try {
                employeeDAO.insert(null, structure, email, name, surname, StaticFunc.formatFinalAddress(state, region, city, street, cap, house_number), phone, password, birth_date, fiscal_code, 'E' );
                inserted = true; /* Se non viene sollevata l'eccezione, l'impiegato è stato inserito correttamente*/
            } catch (DuplicatedObjectException e) {
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            if (inserted) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato inserito correttamente .*/
                applicationMessage = "Employee inserted SUCCESSFULLY.";
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato inserito .*/
                applicationMessage = "Employee insertion ERROR.";

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
        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire un nuovo inserimento */
        request.setAttribute("viewUrl", "admin/new-edit-employee");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (inserted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 6) l'UNICA struttura da mostrare all'interno del text-field readonly */
        request.setAttribute("structure", structure);

        /* TODO: SE SI DECIDE DI IMPLEMENTARE IL CARICAMENTO FOTO PRENDERE SPUNTO DA TALE CODICE */

        /*        List<FileItem> items = (List<FileItem>) request.getAttribute("items");
         *//*Path in cui sono contenute le immagini caricare dall'admin*//*
        String imagePath = request.getServletContext().getRealPath("/img/employees");
        *//*HashMap per contenere i formField che non sono di tipo file*//*
        FileItem imgField = null; *//*item che contiene il file immagine passato con il form */

        /*// Process the uploaded items
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                *//*Filtro i parametri da usare per l'inserimento nel DB*//*
                if (fieldName.equals("name")) name = item.getString();
                else if (fieldName.equals("surname")) surname = item.getString();
                else if (fieldName.equals("sex")) sex = item.getString().charAt(0);
                else if (fieldName.equals("email")) email = item.getString();
                else if (fieldName.equals("phone")) phone = item.getString();
                else if (fieldName.equals("hire_date")) hire_date = LocalDate.parse(item.getString());
                    *//*dal form ricevo solo l'ID della struttura che poi utilizzerò per cercare la riga relativa alla struttura che mi interessa*//*
                else if (fieldName.equals("structure")) {
                    try {
                        id_structure = Long.parseLong(item.getString());
                    } catch (NumberFormatException e) {
                        *//*TODO:da rivedere come gestire tale situazione anche se non dovrebbe accadere in quanto le strutture sono
         *  scaricate dal DB*//*

                        id_structure = 1L; *//*the L is necessary because is a Long*//*
                    }
                }
                else if (fieldName.equals("name")) name = item.getString();
            } else {
                *//*Dato che salverò il file immagine nel filesystem con il nome costituito dal codice fiscale dell'impiegato
         * (così da poterlo sovrascrivere nel momento in cui verrà scelta una nuova immagine), mi salvo l'item
         * all'interno di un FileItem (imgField) così da aggiungere il file solo se l'inserimento sul DB è andato
         * a buon fine.*//*

                imgField = item;
                *//*                String fileExtension = FilenameUtils.getExtension(item.getName()); *//**//*recupero l'estensione del file*//**//*
         *//**//*TODO:CONTROLLARE ESTENSIONE DEL FILE E SE NON RICONOSCIUTA TRA QUELLE VALIDE SETTARE IMMAGINE DI DEFAULT*//**//*
                String uploadedFilePath = imagePath + "/" + fiscal_code + "." + fileExtension;
                File uploadedFile = new File(uploadedFilePath);
                item.write(uploadedFile);*//*
            }
        }*/
    }

    public static void showEmployees(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a DAOFactory to be able to show ALL employees in Database with call to commonView.
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
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "admin/show-employees");

    }

    public static void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates an EmployeeDAO to be able to delete employee from Database.
         */
        Long idToDelete = null; /* id dell'impiegato ricevuto da cancellare*/
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;
        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare la cancellazione del dipendente */
        User user = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;


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

            /* Fetching dell'id dell'impiegato da cancellare proveniente dal form hidden dentro la pagina show-employees.jsp */
            idToDelete = Long.valueOf(request.getParameter("employeeID"));


            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* è necessario prendersi solo lo userDAO per poter settare su uno specifico utente il flag DELETED */
            userDAO = daoFactory.getUserDAO();

            /* trovo l'utente da flaggare come cancellato */
            user = userDAO.findById(idToDelete);

            /* Effettuo la cancellazione del dipendente usando il metodo delete dello UserDAO in quanto il flag DELETED
             * è presente solamente nella tabella USER */

            deleted = userDAO.delete(user); /* Se non viene sollevata l'eccezione, l'impiegato è stato cancellato correttamente*/

            /* Chiamo la commonView che si occuperà di ricaricare la lista dei dipendenti aggiornata, togliendo quelli eliminati
             *  e scaricare le informazioni riguardo la struttura che stiamo gestendo. È necessario chiamarla in quanto la cancellazione,
             * a differenza dell'aggiunta non ha una propria pagina, ma consiste nel click di un semplice bottone ( il cestino ) pertanto è
             * necessario ricaricare la lista dei dipendenti aggiornata ( chiamando appunto la commonView ) e solo settare ( come faccio sotto )
             * la viewUrl alla pagina show-employee.jsp */
            commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */


            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            if (deleted) {
                /* Se l'impiegato è stato  cancellato committo la transazione */
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato cancellato correttamente .*/
                applicationMessage = "Employee deleted SUCCESSFULLY.";
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato cancellato .*/
                applicationMessage = "Employee cancellation ERROR.";

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

        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */
        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-employee.jsp per consentire altre cancellazioni */
        request.setAttribute("viewUrl", "admin/show-employees");
        /* 5) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (deleted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
    }

    public static void showFormEditEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Set viewUrl to the JSP <new-edit-employee> to show the data entry form of the existent employee.
         * Send employee to modify as attribute of the request, to fill in new-edit-employee.jsp automatically
         * field with data of existent employee.
         * */
        Long idToEdit = null; /* Id dell'impiegato da modificare */
        User employeeToEdit = null; /* oggetto di classe User che deve essere passato alla pagina del form di inserimento/modifica */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser = null;
        UserDAO employeeDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
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

            /* Fetching dei parametri provenienti dal form di modifica e salvataggio nelle variabili locali */
            idToEdit = Long.valueOf(request.getParameter("employeeID"));

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            structureDAO = daoFactory.getStructureDAO();
            structure = structureDAO.fetchStructure();

            employeeDAO = daoFactory.getUserDAO();
            employeeToEdit = employeeDAO.findById(idToEdit);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


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
        request.setAttribute("viewUrl", "admin/new-edit-employee"); /* bisogna visualizzare new-edit-employee.jsp */
        /* 5) i dati della struttura sulla quale si sta operando da mostrare nella pagina new-edit-employee.jsp */
        request.setAttribute("structure", structure);
        /* 6) l'oggetto impiegato che deve essere modificato */
        request.setAttribute("employeeToModify", employeeToEdit);
    }

    public static void editEmployee(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an EmployeeDAO to be able to edit the existing employee in Database.
         */

        String submit; /*mi aspetto che il value sia "edit_employee"*/

        DAOFactory daoFactory = null; //per il db
        UserDAO employeeDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        Structure structure = null;
        User employeeToEdit = null;
        User originalEmployee = null; /* l'oggetto intatto ancora con i campi non modificati */
        DAOFactory sessionDAOFactory = null; //per i cookie
        User loggedUser = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;

//        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_employee"*/

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


            employeeDAO = daoFactory.getUserDAO();

            structureDAO = daoFactory.getStructureDAO();


            /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo update() su employeeDAO ) */
            structure = structureDAO.fetchStructure();
            System.err.println(structure);

            /* In caso di modifica, il form contiene un campo hidden con name="employeeId" che mi viene
             * passato dalla JSP e consente di poter scaricare dal DB l'impiegato con quel determinato ID */
            originalEmployee = employeeDAO.findById(Long.valueOf(request.getParameter("employeeId")));

            /* Li tratto come oggetti separati così da poter decideree alla fine, in base all'esito dell'update
             * quale passare alla pagina new-edit-employee.jsp */
            employeeToEdit = employeeDAO.findById(Long.valueOf(request.getParameter("employeeId")));

            /* Setto gli attributi che possono essere stati modificati nel form... ( non sappiamo quali sono
             * stati modificati a priori pertanto dobbiamo settarli tutti indifferentemente */

            /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
             * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
             * */
            employeeToEdit.setStructure(structure);
            employeeToEdit.setEmail(request.getParameter("email"));
            employeeToEdit.setName(request.getParameter("name"));
            employeeToEdit.setSurname(request.getParameter("surname"));
            employeeToEdit.setAddress(StaticFunc.formatFinalAddress(request.getParameter("state"), request.getParameter("region"), request.getParameter("city"), request.getParameter("street"), request.getParameter("cap"), request.getParameter("house_number"))); /* attributo della tabella USER */
            employeeToEdit.setPhone(request.getParameter("phone"));
            /* TODO:modifica della password dell'impiegato */
            employeeToEdit.setBirthDate(LocalDate.parse(request.getParameter("birth_date")));
            employeeToEdit.setFiscalCode(request.getParameter("fiscal_code"));
            employeeToEdit.setType('E');
            employeeToEdit.setBlocked(false);
            employeeToEdit.setDeleted(false);

            /* Effettuo la modifica del dipendente */
            try {
                edited = employeeDAO.update(employeeToEdit);/* Se non viene sollevata l'eccezione, l'impiegato è stato modificato correttamente*/

            } catch (DuplicatedObjectException e) {
                applicationMessage = e.getMessage();
                e.printStackTrace();
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();
            if (edited) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato modificato correttamente .*/
                applicationMessage = "Employee modified SUCCESSFULLY.";
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            }


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
                /* Se viene fatto il rollback della transazione il dipendente non è stato modificato .*/
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
        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */
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
        /* 6) l'impiegato che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("employeeToModify", employeeToEdit);
        } else {
            /* FAIL */
            request.setAttribute("employeeToModify", originalEmployee);
        }
        /* 7) l'UNICA struttura da mostrare all'interno del text-field readonly */
        request.setAttribute("structure", structure);

    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        UserDAO employeeDAO = null;
        StructureDAO structureDAO = null;
        Structure structure = null;
        ArrayList<User> employees = null;

        employeeDAO = daoFactory.getUserDAO();

        structureDAO = daoFactory.getStructureDAO();

        /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo insert() su employeeDAO ) */
        structure = structureDAO.fetchStructure();

        /* Scarico dal DB l'intera lista dei dipendenti */
        employees = employeeDAO.fetchAllOnType('E');

        request.setAttribute("employees", employees);
        request.setAttribute("structure", structure);

    }
}
