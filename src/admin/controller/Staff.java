package admin.controller;

import model.dao.DAOFactory;
import model.dao.EmployeeDAO;
import model.dao.StructureDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.exception.NoEmployeeCreatedException;
import model.mo.Employee;
import model.mo.Structure;
import model.mo.User;
import services.config.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class Staff {

    private Staff() {

    }

    public static void showFormNewEmployee(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Set viewUrl to the JSP <new-edit-employee> to show the data entry form of the new employee.
         * */
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.showFormNewEmployee ==> daoFactory.beginTransaction();");
        }
        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = structureDAO.fetchStructure();

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
        request.setAttribute("viewUrl", "admin/new-edit-employee"); /* bisogna visualizzare new-edit-employee.jsp */

        request.setAttribute("structure", structure);
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

        DAOFactory daoFactory = null;
        EmployeeDAO employeeDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare l'inserimento del dipendente */
        Structure structure = null;

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean inserted = false;

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


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.addEmployee ==> daoFactory.beginTransaction();");
        }

        employeeDAO = daoFactory.getEmployeeDAO();

        structureDAO = daoFactory.getStructureDAO();
        /* è necessario prendersi uno userDAO per poter verificare/inserire all'interno del metodo
         * insert dell'EmployeeDAO se l'utente esiste già e fare tutto le query di inserimento dell'utente necessarie
         * */
        userDAO = daoFactory.getUserDAO();

        /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo insert() su employeeDAO ) */
        structure = structureDAO.fetchStructure();
        System.err.println(structure);
        /* Effettuo l'inserimento del nuovo dipendente */
        try {
            employeeDAO.insert(userDAO, birth_date, fiscal_code, hire_date, structure, email, name, surname, formatFinalAddress(state, region, city, street, cap, house_number), phone, password);
            inserted = true; /* Se non viene sollevata l'eccezione, l'impiegato è stato inserito correttamente*/
        } catch (NoEmployeeCreatedException e) {
            applicationMessage = e.getMessage();
            e.printStackTrace();
        }

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (inserted) {
                /* Se l'impiegato è stato inserito correttamente committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato inserito correttamente .*/
                applicationMessage = "Employee inserted SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione il dipendente non è stato inserito .*/
                applicationMessage = "Employee insertion ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire un nuovo inserimento */
        request.setAttribute("viewUrl", "admin/new-edit-employee");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (inserted) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) l'UNICA struttura da mostrare all'interno del text-field readonly */
        request.setAttribute("structure", structure);
    }

    public static void showEmployees(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates a DAOFactory to be able to show ALL employees in Database with call to commonView.
         */
        DAOFactory daoFactory = null;


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.showEmployees ==> daoFactory.beginTransaction();");
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

        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        request.setAttribute("viewUrl", "admin/show-employees");


    }

    public static void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * Instantiates an EmployeeDAO to be able to delete employee from Database.
         */
        Long idToDelete = null; /* id dell'impiegato ricevuto da cancellare*/

        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare la cancellazione del dipendente */
        User user = null;
        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean deleted = false;

        /* Fetching dell'id dell'impiegato da cancellare proveniente dal form hidden dentro la pagina show-employees.jsp */
        idToDelete = Long.valueOf(request.getParameter("employeeID"));

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

        /* Effettuo la cancellazione del dipendente usando il metodo delete dello UserDAO in quanto il flag DELETED
         * è presente solamente nella tabella USER */

        deleted = userDAO.delete(user); /* Se non viene sollevata l'eccezione, l'impiegato è stato cancellato correttamente*/

        /* Chiamo la commonView che si occuperà di ricaricare la lista dei dipendenti aggiornata, togliendo quelli eliminati
         *  e scaricare le informazioni riguardo la struttura che stiamo gestendo. È necessario chiamarla in quanto la cancellazione,
         * a differenza dell'aggiunta non ha una propria pagina, ma consiste nel click di un semplice bottone ( il cestino ) pertanto è
         * necessario ricaricare la lista dei dipendenti aggiornata ( chiamando appunto la commonView ) e solo settare ( come faccio sotto )
         * la viewUrl alla pagina show-employee.jsp */
        commonView(daoFactory, request); /* !!! ATTENZIONE A CHIAMARLA PRIMA DI CHIUDERE LA CONNESSIONE CON IL DATABASE */

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (deleted) {
                /* Se l'impiegato è stato inserito cancellato committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato cancellato correttamente .*/
                applicationMessage = "Employee deleted SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione il dipendente non è stato cancellato .*/
                applicationMessage = "Employee cancellation ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }


        /* Setto gli attributi della request che verranno processati dalla show-employees.jsp */

        /* 1) il messaggio da visualizzare nella pagina di elenco solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la cancellazione ==> viene visualizzata nuovamente
         *     la show-employee.jsp per consentire altre cancellazioni */
        request.setAttribute("viewUrl", "admin/show-employees");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
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
        idToEdit = Long.valueOf(request.getParameter("employeeID"));
        Employee employeeToEdit = null; /* oggetto di classe Employee che deve essere passato alla pagina del form di inserimento/modifica */

        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.showFormEditEmployee ==> daoFactory.beginTransaction();");
        }
        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = structureDAO.fetchStructure();

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        employeeToEdit = employeeDAO.findById(idToEdit);

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
        request.setAttribute("viewUrl", "admin/new-edit-employee"); /* bisogna visualizzare new-edit-employee.jsp */
        /* 2) i dati della struttura sulla quale si sta operando da mostrare nella pagina new-edit-employee.jsp */
        request.setAttribute("structure", structure);
        /* 3) l'oggetto impiegato che deve essere modificato */
        request.setAttribute("employeeToModify", employeeToEdit);
    }

    public static void editEmployee(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Instantiates an EmployeeDAO to be able to edit the existing employee in Database.
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
        String submit; /*mi aspetto che il value sia "edit_employee"*/
        String password; /*password generata nel controller e NON proveniente dal form*/

        DAOFactory daoFactory = null;
        EmployeeDAO employeeDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        StructureDAO structureDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare la modifica del dipendente */
        Structure structure = null;
        Employee employeeToEdit = null;
        Employee originalEmployee = null; /* l'oggetto intatto ancora con i campi non modificati */

        String applicationMessage = "An error occurred!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean edited = false;

        /* Fetching dei parametri provenienti dal form di inserimento/modifica e salvataggio nelle variabili locali */
        name = request.getParameter("name");/*required*/
        surname = request.getParameter("surname");/*required*/
        email = request.getParameter("email");/*required*/
        phone = request.getParameter("phone");/*required*/
        /*In this case string is in ISO_LOCAL_DATE format, then we can parse the String directly without DateTimeFormatter
         * The ISO date formatter that formats or parses a date without an offset, such as '2011-12-03'.
         * */
        hire_date = LocalDate.parse(request.getParameter("hire_date"));/*required*/
        birth_date = LocalDate.parse(request.getParameter("birth_date"));/*required*/
        fiscal_code = request.getParameter("fiscal_code");/*required*/
        state = request.getParameter("state"); /*required*/
        region = request.getParameter("region");/*required*/
        city = request.getParameter("city");/*required*/
        street = request.getParameter("street");/*required*/
        house_number = request.getParameter("house_number");/*not required*/
        cap = request.getParameter("cap");/*not required*/
        /*   CREAZIONE DELLA PASSWORD SUPER SICURA!!!  */
        password = "password";
        submit = request.getParameter("submit"); /*mi aspetto che il value sia "edit_employee"*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Staff.addEmployee ==> daoFactory.beginTransaction();");
        }

        employeeDAO = daoFactory.getEmployeeDAO();

        structureDAO = daoFactory.getStructureDAO();
        /* è necessario prendersi uno userDAO per poter verificare/inserire all'interno del metodo
         * insert dell'EmployeeDAO se esiste un utente con i dati coincidenti con quelli nuovi, infatti devo
         * negare la possibilità di modificare un dipendente con dati di un altro dipendente
         * */
        userDAO = daoFactory.getUserDAO();

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

        employeeToEdit.getUser().setName(name); /* attributo della tabella USER */
        employeeToEdit.getUser().setSurname(surname); /* attributo della tabella USER */
        employeeToEdit.getUser().setEmail(email); /* attributo della tabella USER */
        employeeToEdit.getUser().setPhone(phone); /* attributo della tabella USER */
        employeeToEdit.getUser().setAddress(formatFinalAddress(state, region, city, street, cap, house_number)); /* attributo della tabella USER */
        employeeToEdit.getUser().setName(name); /* attributo della tabella USER */
        employeeToEdit.getUser().setIsAdmin(false); /* attributo della tabella USER */
        employeeToEdit.getUser().setIsEmployee(true); /* attributo della tabella USER */
        employeeToEdit.getUser().setIsCustomer(false); /* attributo della tabella USER */
        employeeToEdit.getUser().setIsDeleted(false); /* attributo della tabella USER */
        employeeToEdit.setBirthDate(birth_date); /* attributo della tabella EMPLOYEE */
        employeeToEdit.setHireDate(hire_date); /* attributo della tabella EMPLOYEE */
        employeeToEdit.setFiscalCode(fiscal_code); /* attributo della tabella EMPLOYEE */
        employeeToEdit.setStructure(structure); /* attributo della tabella EMPLOYEE */

        /* Effettuo la modifica del dipendente */
        try {
            edited = employeeDAO.update(employeeToEdit);/* Se non viene sollevata l'eccezione, l'impiegato è stato modificato correttamente*/

        } catch (DuplicatedObjectException e) {
            applicationMessage = e.getMessage();
            e.printStackTrace();
        }

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (edited) {
                /* Se l'impiegato è stato modificato correttamente committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che il dipendente sia stato modificato correttamente .*/
                applicationMessage = "Employee modified SUCCESSFULLY.";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione il dipendente non è stato modificato .*/
                applicationMessage = "Employee modification ERROR.";

            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla new-edit-employee.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato l'inserimento ==> viene visualizzato nuovamente il
         *     form per consentire ulteriori modifiche sul medesimo impiegato */
        request.setAttribute("viewUrl", "admin/new-edit-employee");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) l'impiegato che è stato modificato e i cui dati aggiornati( o meno ) verranno mostrati nuovamente nella pagina*/
        if (edited) {
            /* SUCCESS */
            request.setAttribute("employeeToModify", employeeToEdit);
        } else {
            /* FAIL */
            request.setAttribute("employeeToModify", originalEmployee);
        }
        /* 5) l'UNICA struttura da mostrare all'interno del text-field readonly */
        request.setAttribute("structure", structure);
    }

    private static String formatFinalAddress(String state, String region, String city, String street, String cap, String house_number) {
        String mandatory = state + "|" + region + "|" + city + "|" + cap + "|" + street;
        if (!house_number.equals(" "))
            mandatory = mandatory + "|" + house_number + "|"; /*!!! IMPORTANTE METTERE LA PIPE ALLA FINE ALTRIMENTI LO SPLIT NON FUNZIONA*/
        else
            mandatory = mandatory + "|" + " " + "|"; /* aggiungo comunque la | così quando devo splittare l'indirizzo mi ritorna stringa vuota*/
        return mandatory;
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {
        EmployeeDAO employeeDAO = null;
        StructureDAO structureDAO = null;
        Structure structure = null;
        ArrayList<Employee> employees = null;

        employeeDAO = daoFactory.getEmployeeDAO();

        structureDAO = daoFactory.getStructureDAO();

        /* Scarico dal DB l'UNICA struttura ( che passo poco sotto al metodo insert() su employeeDAO ) */
        structure = structureDAO.fetchStructure();

        /* Scarico dal DB l'intera lista dei dipendenti */
        employees = employeeDAO.fetchAll();


        request.setAttribute("employees", employees);
        request.setAttribute("structure", structure);

    }
}
