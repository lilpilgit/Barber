package home.controller;

import functions.StaticFunc;
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
import java.util.ArrayList;
import java.util.HashMap;

public class Home {

    private Home() {
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call the common/home.jsp
         * */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        UserDAO sessionUserDAO = null;
        UserDAO userDAO = null;
        User loggedUser = null;
        boolean cookieValid = true;

        try {

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory = initializeCookie(request, response);

            sessionDAOFactory.beginTransaction();

            sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/

            /* Controllo se è presente un cookie di sessione tra quelli passati dal browser */
            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            /* controllo lo stato dell'utente */
            if (loggedUser != null) {
                /* c'è un utente loggato */
                if (!sessionUserDAO.isValid(userDAO.findById(loggedUser.getId()))) {
                    /* utente non autorizzato, invalido il cookie */
                    System.out.println("UTENTE NON AUTORIZZATO !");
                    home.controller.Home.logout(request, response);
                    cookieValid = false;
                }
            } else {
                /* La pagina Home è pubblica anche per gli utenti non loggati */
            }

            /* verifico se devo eseguire la logica di business o meno */
            if (cookieValid) {
                /* Chiamo la commonView */
                commonView(daoFactory, request);
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");

            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        if (cookieValid) {
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/home");
        }
    }

    public static void logon(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check validity of credentials on DB and set cookie. After that call common/home.jsp
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        String applicationMessage = null;
        UserDAO sessionUserDAO = null;
        User loggedUser = null;
        UserDAO userDAO = null;
        User user = null;
        boolean viewEmployee = false; /* viene settato a true solo se si logga il dipendente */

        try {

            sessionDAOFactory = initializeCookie(request, response);
            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/

            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            /* check delle credenziali sul database */
            userDAO = daoFactory.getUserDAO();

            user = userDAO.findByEmail(email); /* tale utente esiste???? */

            /* se l'utente con tale email non esiste oppure ha inserito una password sbagliata */
            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.delete(null); /* crea un cookie vuoto */
                applicationMessage = "Wrong username or password!";
                loggedUser = null;
            } else {
                /* determino il tipo di utente */
                System.err.println("UTENTE CHE SI È LOGGATO =======> " + user);
                switch (user.getType()) {
                    case 'A':
                        /* ADMIN */
                        /* ulteriori operazioni */
                        loggedUser = sessionUserDAO.insert(user.getId(), null, null, null, null, null, null, null, null, null, user.getType());
                        break;
                    case 'E':
                        /* EMPLOYEE */
                        loggedUser = sessionUserDAO.insert(user.getId(), null, null, null, null, null, null, null, null, null, user.getType());
                        viewEmployee = true;
                        break;
                    case 'C':
                        /* CUSTOMER */
                        /* email e password corretta, utente non cancellato, è un cliente e verifico se è BLOCCATO */
                        if (user.getId() != null && user.isBlocked()) { /* mettendo solo user != null va in errore */
                            sessionUserDAO.delete(null); /* crea un cookie vuoto */
                            applicationMessage = "Your account has been blocked. Contact us for further information.";
                            loggedUser = null;
                        } else {
                            loggedUser = sessionUserDAO.insert(user.getId(), null, null, null, null, null, null, null, null, null, user.getType());
                        }
                        break;
                    default:
                        /* ERROR!!!! */
                        System.err.println("ERRORE NELLO SWITCH CASE!!!!!");
                }


            }
            if (!viewEmployee) {
                /* Chiamo la commonView solo se si sta loggando il cliente o l'admin */
                commonView(daoFactory, request);
            }

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {

            try {
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }

        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        System.err.println("loggedOn==> " + loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        System.err.println("loggedUser=> " + loggedUser);
        /* 3) Application message da mostrare all'utente */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 4) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (loggedUser != null) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 5) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "common/home");
        /* Chiamo il metodo del controller che si occupa di settare i dati necessari a visualizzare la giornata lavorativa dell'impiegato */
        if (viewEmployee) {
            employee.controller.Work.showBookings(request, response);
        }

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        /**
         * Invalidate with max-age session cookie.
         */
        DAOFactory sessionDAOFactory = null;//per i cookie
        UserDAO sessionUserDAO = null;
        DAOFactory daoFactory = null; //per il db

        try {

            sessionDAOFactory = initializeCookie(request,response);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            sessionUserDAO = sessionDAOFactory.getUserDAO(); /* Ritorna: new UserDAOCookieImpl(request, response);*/
            sessionUserDAO.delete(null); /* new Cookie("loggedUser", ""); +  cookie.setMaxAge(0);*/

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", false);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", null);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "common/home");

    }

    public static void register(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Verify if exist yet user with same email and register new user.
         */

        String name;
        String surname;
        String email;
        String password;
        String phone;
        String state;
        String region;
        String city;
        String cap;
        String street;
        String house_number;

        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare l'inserimento dell'utente  */

        String applicationMessage = "It was not possible to register!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean registered = false;


        try {

            /* Fetching dei parametri provenienti dal form di registrazione*/
            email = request.getParameter("email");/*required*/
            name = request.getParameter("name");/*required*/
            surname = request.getParameter("surname");/*required*/
            password = request.getParameter("password"); /*required*/
            state = request.getParameter("state"); /*required*/
            region = request.getParameter("region");/*required*/
            city = request.getParameter("city");/*required*/
            cap = request.getParameter("cap");/*required*/
            street = request.getParameter("street");/*required*/
            house_number = request.getParameter("house_number");/*not required*/
            phone = request.getParameter("phone");/*required*/

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            userDAO = daoFactory.getUserDAO();

            try {
                userDAO.insert(null, null, email, name, surname, StaticFunc.formatFinalAddress(state, region, city, street, cap, house_number), phone, password, null, null, 'C');
                registered = true; /* se non viene sollevata l'eccezione riesco a settarlo a true */

            } catch (DuplicatedObjectException e) {
                System.out.println("HOME.JAVA ==> Errore durante la registrazione.");
                applicationMessage = "Email already registered";
            }
            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit sul db */
            daoFactory.commitTransaction();

            if (registered) {
                /* Solo se viene committata la transazione senza errori siamo sicuri che il cliente sia stato registrato correttamente .*/
                applicationMessage = "Registration successful. Now you can Login!";
            }
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback sul db*/
                /* Se viene fatto il rollback della transazione il cliente non è stato registrato .*/
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
            } catch (Throwable t) {
                System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
            }
        } finally {
            try {
                /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
                if (daoFactory != null) daoFactory.closeTransaction(); /* Close sul db*/
                System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
            } catch (Throwable t) {
            }
        }

        /* Setto gli attributi della request che verranno processati dalla home.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la registrazione */
        request.setAttribute("viewUrl", "common/home");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (registered) {
            /* SUCCESS */
            request.setAttribute("result", "success");
        } else {
            /* FAIL */
            request.setAttribute("result", "fail");
        }
        /* 4) il booleano per sapere se è loggato o meno ( dopo la registrazione non posso essere loggato ) */
        request.setAttribute("loggedOn", false);
        /* 5) oggetto corrispondente all'utente loggato ( dopo la registrazione non posso essere loggato dunque null ) */
        request.setAttribute("loggedUser", null);

    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {

        ArrayList<Product> showcase = null;
        ProductDAO productDAO = daoFactory.getProductDAO();
        StructureDAO structureDAO = daoFactory.getStructureDAO();
        Structure structure = null;

        /* Scarico dal DB la lista dei prodotti da mostrare in vetrina */
        showcase = productDAO.findShowcaseProduct();

        /* Setto i prodotti della vetrina come parametro della request */
        request.setAttribute("showcase", showcase);

        /* Scarico dal DB l'unica struttura */
        structure = structureDAO.fetchStructure();

        /* Setto l'oggetto struttura da mostrare in ogni footer dell'area customer */
        request.setAttribute("structure", structure);

    }

    private static DAOFactory initializeCookie(HttpServletRequest request, HttpServletResponse response) {
        /* Inizializzo il cookie di sessione */
        HashMap sessionFactoryParameters = new HashMap<String, Object>();
        sessionFactoryParameters.put("request", request);
        sessionFactoryParameters.put("response", response);
        return DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

    }

}
