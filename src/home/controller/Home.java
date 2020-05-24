package home.controller;

import functions.StaticFunc;
import model.dao.CustomerDAO;
import model.dao.DAOFactory;
import model.dao.ProductDAO;
import model.dao.UserDAO;
import model.exception.NoCustomerCreatedException;
import model.mo.Product;
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


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            /* 3) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/home");
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

    }

    public static void logon(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check validity of creds on DB and set cookie. After that call common/home.jsp
         */
        DAOFactory sessionDAOFactory = null; //per i cookie
        DAOFactory daoFactory = null; //per il db
        User loggedUser;
        String applicationMessage = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/
            loggedUser = sessionUserDAO.findLoggedUser();


            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            /* check delle credenziali sul database */
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByEmail(email); /* tale utente esiste???? */

            /* se l'utente con tale email non esiste oppure ha inserito una password sbagliata */
            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.delete(null);
                applicationMessage = "Username e/o password errati!";
                loggedUser = null;
            } else {
                loggedUser = sessionUserDAO.insert(user.getId(), user.getEmail(), user.getName(), user.getSurname(), null, null, null, user.isAdmin(), user.isEmployee(), user.isCustomer());
            }


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedUser != null);
            System.err.println("loggedOn==> " + loggedUser != null);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/home");
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

    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        /**
         * Invalidate with max-age session cookie.
         */
        DAOFactory sessionDAOFactory = null;//per i cookie
        DAOFactory daoFactory = null; //per il db

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
            sessionUserDAO.delete(null); /* new Cookie("loggedUser", ""); +  cookie.setMaxAge(0);*/

            sessionDAOFactory.commitTransaction(); /* Commit fittizio */

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();


            /* Chiamo la commonView */
            commonView(daoFactory, request);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();


            request.setAttribute("loggedOn", false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "common/home");

        } catch (Exception e) {
            try {
                /* Rollback fittizio */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                /* Close fittizia */
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }


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
        String fiscal_code;
        String state;
        String region;
        String city;
        String cap;
        String street;
        String house_number;

        DAOFactory daoFactory = null;
        CustomerDAO customerDAO = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare l'inserimento del cliente */

        String applicationMessage = "It was not possible to register!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean registered = false;


        /* Fetching dei parametri provenienti dal form di registrazione*/
        name = request.getParameter("name");/*required*/
        surname = request.getParameter("surname");/*required*/
        email = request.getParameter("email");/*required*/
        password = request.getParameter("password"); /*required*/
        phone = request.getParameter("phone");/*required*/
        state = request.getParameter("state"); /*required*/
        region = request.getParameter("region");/*required*/
        city = request.getParameter("city");/*required*/
        cap = request.getParameter("cap");/*required*/
        street = request.getParameter("street");/*required*/
        house_number = request.getParameter("house_number");/*not required*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Home.register ==> daoFactory.beginTransaction();");
        }

        customerDAO = daoFactory.getCustomerDAO();

        userDAO = daoFactory.getUserDAO();

        /* Effettuo l'inserimento del nuovo cliente */
        try {
            customerDAO.insert(userDAO, email, name, surname, StaticFunc.formatFinalAddress(state, region, city, street, cap, house_number), phone, password);
            registered = true; /* Se non viene sollevata l'eccezione, l'impiegato è stato inserito correttamente*/
        } catch (NoCustomerCreatedException e) {
            /* cliente già registrato con la stessa email */
            applicationMessage = "Already registered user.";
            e.printStackTrace();
        }

        /* Chiamo la commonView */
        commonView(daoFactory, request);

        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            if (registered) {
                /* Se il cliente è stato inserito correttamente committo la transazione */
                daoFactory.commitTransaction();
                System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Solo se viene committata la transazione senza errori siamo sicuri che il cliente sia stato inserito correttamente .*/
                applicationMessage = "Registration successful. Now you can Login!";

            } else {
                /* Altrimenti faccio il rollback della transazione */
                daoFactory.rollbackTransaction();
                System.err.println("ROLLBACK DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");

                /* Se viene fatto il rollback della transazione il cliente non è stato inserito .*/
            }
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla home.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la registrazione */
        request.setAttribute("viewUrl", "common/home");
        /* 3) l'attributo booleano result così da facilitare la scelta dei colori nel frontend JSP ( rosso ==> errore, verde ==> successo per esempio )*/
        if (registered) {
            /* SUCCESS */
            request.setAttribute("registered", true);
        } else {
            /* FAIL */
            request.setAttribute("registered", false);
        }
        /* 4) il booleano per sapere se è loggato o meno ( dopo la registrazione non posso essere loggato ) */
        request.setAttribute("loggedOn", false);
        /* 4) oggetto corrispondente all'utente loggato ( dopo la registrazione non posso essere loggato dunque null ) */
        request.setAttribute("loggedUser", null);


    }

    public static void showBook(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call cart.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/book");
    }

    public static void showShop(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call shop.jsp
         */

        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        ProductDAO productDAO = null; /* per fetchare i prodotti */
        User loggedUser = null;
        ArrayList<Product> products = null; /* prodotti fetchati dal db da mostrare nella pagina shop */
        String applicationMessage = null;
        ArrayList<String> categories = null; /*categorie da mostrare nel dropdown del filtro */
        ArrayList<String> brands = null; /*produttori da mostrare nel dropdown del filtro */
        String categoryToFilter = "All"; /* voce predefinita nel filtro delle categorie */
        String brandToFilter = "All"; /* voce predefinita nel filtro dei brands */

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();


            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/

            loggedUser = sessionUserDAO.findLoggedUser();


            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);


            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Istanzio un DAO per poter fetchare i prodotti */
            productDAO = daoFactory.getProductDAO();

            /* Prendo tutte le categorie dal database */
            categories = productDAO.findAllCategories();

            /* Prendo tutti i produttori dal database */
            brands = productDAO.findAllProducers();

            /* Fetching dei parametri  */
            String toFilter = request.getParameter("filter");
            System.err.println("toFilter ==>" + toFilter);
            int filter = 0; /* ipotizzo che non venga richiesto il filtraggio dei prodotti */
            if (toFilter != null) {
                /* posso provare a parsarlo per evitare NullPointerException*/
                filter = Integer.parseInt(toFilter);
            }
            System.err.println("filter:" + filter);
            if (filter == 1) {
                brandToFilter = request.getParameter("brand");
                categoryToFilter = request.getParameter("category");

                products = productDAO.findFilteredProducts((categoryToFilter.equals("All")) ? "%" : categoryToFilter, (brandToFilter.equals("All")) ? "%" : brandToFilter);

            } else {
                products = productDAO.findAllProducts();
            }



            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            boolean loggedOn = loggedUser != null;
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedOn);

            System.err.println("loggedOn==>" + loggedOn);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/shop");
            /* 5) Setto la lista dei prodotti da mostrare */
            request.setAttribute("products", products);
            /* 6) Setto la lista delle categorie completa */
            request.setAttribute("categories", categories);
            /* 7) Setto la lista dei brand completa */
            request.setAttribute("brands", brands);
            /* 8) Setto il brand che era stato selezionato per poterlo mostrare nella pagina filtrata all'interno del dropdown */
            request.setAttribute("brandFiltered", brandToFilter);
            /* 9) Setto la categoria che era stata selezionata per poterla mostrare nella pagina filtrata all'interno del dropdown */
            request.setAttribute("categoryFiltered", categoryToFilter);
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
    }

    public static void contact(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Process the request, FAKE: send email, and inform user of result.
         */
        String name;
        String email;
        String text;

        DAOFactory daoFactory = null;
        UserDAO userDAO = null; /* DAO Necessario per poter effettuare l'inserimento del cliente */

        String applicationMessage = "It was not possible to send email. Try later!"; /* messaggio da mostrare a livello applicativo ritornato dai DAO */
        boolean contacted = false; /* risultato dell'operazione di invio di una mail */
        User user = null; /* utente che sta usando la form di contatto */
        boolean registered = false; /* per sapere se l'utente che contatta è già registrato o meno */

        /* Fetching dei parametri provenienti dal form di registrazione*/
        name = request.getParameter("contact_name");/*required*/
        email = request.getParameter("contact_email");/*required*/
        text = request.getParameter("contact_message");/*not required*/


        daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
        if (daoFactory != null) {
            daoFactory.beginTransaction();
        } else {
            throw new RuntimeException("Errore nel Controller Home.register ==> daoFactory.beginTransaction();");
        }

        userDAO = daoFactory.getUserDAO();

        /* Verifico se è  un cliente già registrato o meno TODO:per ulteriori statistiche */

        user = userDAO.findByEmail(email);
        if (user != null) {
            /* utente già registrato */
            registered = true;
        }
        /* TODO: operazioni di invio email*/
        contacted = true; /* invio mail sempre a buon fine */
        System.out.println("Invio dell'email da parte di " + name + "----" + email + "----" + text);
        applicationMessage = "Message sent correctly. We will contact you as soon as possible.";


        /* Effettuo le ultime operazioni di commit o rollback e poi successiva chiusura della transazione */
        try {
            daoFactory.commitTransaction();
            System.err.println("COMMIT DELLA TRANSAZIONE AVVENUTO CON SUCCESSO");
        } catch (Exception e) {
            System.err.println("ERRORE NEL COMMIT/ROLLBACK DELLA TRANSAZIONE");
        } finally {
            /* Sia in caso di commit che in caso di rollback chiudo la transazione*/
            daoFactory.closeTransaction();
            System.err.println("CHIUSURA DELLA TRANSAZIONE AVVENUTA CON SUCCESSO");
        }

        /* Setto gli attributi della request che verranno processati dalla contact.jsp */

        /* 1) il messaggio da visualizzare nella pagina di inserimento solo se non è null */
        request.setAttribute("applicationMessage", applicationMessage);
        /* 2) l'url della pagina da visualizzare dopo aver effettuato la registrazione */
        request.setAttribute("viewUrl", "common/contact");
        /* 3) il booleano per sapere se è loggato o meno ( dopo la registrazione non posso essere loggato ) */
        request.setAttribute("loggedOn", false);
        /* 4) oggetto corrispondente all'utente loggato ( dopo la registrazione non posso essere loggato dunque null ) */
        request.setAttribute("loggedUser", null);
        /* 5) oggetto corrispondente al risultato dell'operazione */
        request.setAttribute("contacted", contacted);

    }

    public static void showContactForm(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call contact.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "common/contact");
    }

    public static void showCart(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call cart.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/cart");
    }

    public static void showWishlist(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call wishlist.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/wishlist");
    }

    public static void showBookings(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call profile.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/bookings");
    }

    public static void showOrders(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call profile.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/orders");
    }

    public static void showProfile(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call profile.jsp
         */

        DAOFactory sessionDAOFactory = null; //per i cookie
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


            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();/* Close fittizia */
            } catch (Throwable t) {
            }
        }


        /* 1) Attributo che indica se è loggato oppure no */
        request.setAttribute("loggedOn", loggedUser != null);
        /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
        request.setAttribute("loggedUser", loggedUser);
        /* 3) Setto quale view devo mostrare */
        request.setAttribute("viewUrl", "customer/profile");
    }

    public static void showProduct(HttpServletRequest request, HttpServletResponse response) {
        /**
         * Check if user is logged then call product.jsp with id of product to show.
         */

        DAOFactory daoFactory = null; //per il db
        DAOFactory sessionDAOFactory = null; //per i cookie
        ProductDAO productDAO = null; /* per fetchare il prodotto */
        User loggedUser = null;
        Product product = null; /* prodotto fetchato dal db da mostrare nella pagina product.jsp */
        String applicationMessage = null;

        try {
            /* Inizializzo il cookie di sessione */
            HashMap sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);

            /* Come in una sorta di connessione al DB, la beginTransaction() per i cookie setta
             *  nel costruttore di CookieDAOFactory la request e la response presenti in sessionFactoryParameters*/
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();/* Ritorna: new UserDAOCookieImpl(request, response);*/

            loggedUser = sessionUserDAO.findLoggedUser();

            /* DAOFactory per manipolare i dati sul DB */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            /* Inizio la transazione sul Database*/
            daoFactory.beginTransaction();

            /* Istanzio un DAO per poter fetchare i prodotti */
            productDAO = daoFactory.getProductDAO();


            /* Fetching dei parametri  */
            String idProduct = request.getParameter("idProduct");
            System.err.println("idProduct ==>" + idProduct);
            Long id = 1L;
            if (idProduct != null) {
                /* posso provare a parsarlo per evitare NullPointerException*/
                id = Long.parseLong(idProduct);
            }
            System.err.println("id:" + id);

            product = productDAO.findProductById(id);

            /* Commit della transazione sul db */
            daoFactory.commitTransaction();

            /* Commit fittizio */
            sessionDAOFactory.commitTransaction();


            boolean loggedOn = loggedUser != null;
            /* 1) Attributo che indica se è loggato oppure no */
            request.setAttribute("loggedOn", loggedOn);
            System.err.println("loggedOn==>" + loggedOn);
            /* 2) Attributo che indica quale utente è loggato ( da leggere solo se loggedOn = true */
            request.setAttribute("loggedUser", loggedUser);
            System.err.println("loggedUser=> " + loggedUser);
            /* 3) Application messagge da mostrare all'utente */
            request.setAttribute("applicationMessage", applicationMessage);
            /* 4) Setto quale view devo mostrare */
            request.setAttribute("viewUrl", "common/product");
            /* 5) Setto il prodotto da mostrare */
            request.setAttribute("product", product);

        } catch (
                Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction(); /* Rollback della transazione sul db */
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();/* Rollback fittizio */

            } catch (Throwable t) {
            }
        }
    }

    private static void commonView(DAOFactory daoFactory, HttpServletRequest request) {

        ArrayList<Product> showcase = null;
        ProductDAO productDAO = daoFactory.getProductDAO();


        /* Scarico dal DB la lista dei prodotti da mostrare in vetrina */
        showcase = productDAO.findShowcaseProduct();

        /* Setto i prodotti della vetrina come parametro della request */
        request.setAttribute("showcase", showcase);


    }


}
