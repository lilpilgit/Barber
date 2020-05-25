package model.dao.mySQLJDBCImpl;

import model.dao.CustomerDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.exception.NoCustomerCreatedException;
import model.exception.NoEmployeeCreatedException;
import model.mo.Customer;
import model.mo.Employee;
import model.mo.User;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOMySQLJDBCImp implements CustomerDAO {

    private final String COUNTER_ID = "userId"; /* Customer non ha una propria entry nella table COUNTER in quanto il suo id è strettamente legato a quello dell'utente*/
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public CustomerDAOMySQLJDBCImp(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer insert(UserDAO userDAO,
                           String email, /* [+] attributo della table USER */
                           String name, /* [+] attributo della table USER */
                           String surname, /* [+] attributo della table USER */
                           String address, /* [+] attributo della table USER */
                           String phone, /* [+] attributo della table USER */
                           String password /* [+] attributo della table USER */) throws NoCustomerCreatedException {
        /**
         * The entity customer has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method checked if the customer with the email passed as parameter (String email)
         * already exists (in which case raises a NoCustomerCreatedException), inserts the user with ID manually
         * increased on the COUNTER table and only if no exception is raised, inserts the <Customer customer> object
         * in the CUSTOMER table.
         *
         * @return Return the object inserted correctly in the DB otherwise raise an exception.
         * */

        /* Per i clienti basta la sola email per poterne verificare l'esistenza nel DB prima di inserirlo
        * pertanto sfrutto il controllo che viene già fatto nel metodo insert() dello UserDAO*/

        Customer customer = new Customer();
        Long newId = null;


        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO CLIENTE DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER CLIENTI DIVERSI SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/

        query = "UPDATE COUNTER SET VALUE = VALUE + 1 WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, COUNTER_ID);
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }

        /*LEGGO L'ID APPENA PRIMA INCREMENTATO PER POTERLO USARE ALL'INTERNO DELLA INSERT*/
        query = "SELECT VALUE FROM COUNTER WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, COUNTER_ID);
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeQuery();");
            throw new RuntimeException(e);
        }

        /*SPOSTO IL PUNTATORE DEL RESULT SET SULLA PRIMA ( E UNICA IN QUESTO CASO ) RIGA RITORNATA DALLA QUERY*/
        try {
            rs.next();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next();");
            throw new RuntimeException(e);
        }

        /*      !!! SALVO IL NUOVO ID NELLA VARIABILE newId !!!      */
        try {
            newId = rs.getLong("VALUE");
        } catch (SQLException e) {
            System.err.println("Errore nella newId = rs.getLong(\"VALUE\");");
            throw new RuntimeException(e);
        } finally {
            /* IL resultSet una volta letto l'id non serve più in quanto rimane da fare solo l'INSERT di USER e poi CUSTOMER */
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        /* L'unico campo del cliente che rimane da settare è l'ID. Setto l'id del cliente con il nuovo ID. */
        customer.setId(newId);

        /*              MOLTO IMPORTANTE !!!!!
            Viene prima aggiunto l'utente e se l'inserimento è andato a buon fine verrà aggiunto anche il cliente
            con lo stesso ID. Notare che viene usato lo userDAO istanziato nella transazione dal controller,
            perché deve essere uno userDAO istanziato all'interno della medesima transazione con il DB
         */

        try {
            User user = userDAO.insert(newId, email, name, surname, address, phone, password, false, false, true);
            /*                                                                                                                ^^^^^^^^^^^^^^ ==> STIAMO INSERENDO UN UTENTE COME CLIENTE!!!*/
            /*Setto l'unico campo rimanente che risulta essere user all'interno dell'oggetto customer da inserire nel DB*/
            customer.setUser(user);

        } catch (DuplicatedObjectException e) {
            throw new NoCustomerCreatedException("CustomerDAOJDBCImpl.insert: Tentativo di inserimento di un cliente già esistente con email : {" + email + "}.");
        }

        /* Se non è stata sollevata alcuna eccezione fin qui, allora:

                >il CLIENTE NON ESISTE GIÀ
                >lo USER NON ESISTE GIÀ CON TALE EMAIL

           Pertanto provo a inserirlo nel DB
        */

        query = "INSERT INTO CUSTOMER(ID, NUM_BOOKED_RESERVATIONS, NUM_ORDERED_PRODUCT,BLOCKED) VALUES(?,0,0,0);";
        try {                                                                                     //     ^^^^^ CLIENTE APPENA REGISTRATO
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, customer.getId());
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }

        /* Eseguo l'inserimento */
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate()");
            throw new RuntimeException(e);
        }

        /*Chiudo il preparedStatement*/
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /*
         * Se non è stata sollevata alcuna eccezione fin qui, ritorno correttamente l'oggetto di classe Customer
         * appena inserito
         * */

        return customer;
    }

    @Override
    public ArrayList<Customer> fetchAll() {
        /**
         * The entity customer has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method extracts all data of all customer including those present in the user table by
         * performing a JOIN of the 2 tables on the ID field.
         *
         * @return Return the ArrayList<Customers> with all customer that are not deleted in the UNIQUE structure
         * otherwise raise an exception.
         * */

        ArrayList<Customer> customers = new ArrayList<>();
        /* Seleziono solamente i clienti che non sono stati cancellati */
        query = "SELECT * FROM CUSTOMER C INNER JOIN USER U ON C.ID = U.ID WHERE DELETED = 0;";

        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            while (rs.next()) { /* Fin tanto che esiste un cliente */
                customers.add(readCustomerWithUserFields(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella customers.add(readCustomerWithUserFields(rs))");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close()");
            throw new RuntimeException(e);
        }

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        return customers;
    }

    @Override
    public Customer findById(Long id) {
        Customer customer = new Customer();
        query = "SELECT * FROM CUSTOMER C INNER JOIN USER U ON C.ID = U.ID WHERE DELETED = 0 AND C.ID = ?;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, id);
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            if (rs.next()) {
                /*Se true significa che esiste un cliente con quell'ID*/
                customer = readCustomerWithUserFields(rs);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella if(rs.next())");
            throw new RuntimeException(e);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public boolean blockCustomer(Customer customer) {

        query = "UPDATE CUSTOMER SET BLOCKED = '1' WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, customer.getId());
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean unBlockCustomer(Customer customer) {

        query = "UPDATE CUSTOMER SET BLOCKED = '0' WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, customer.getId());
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }
        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean update(Customer customer) throws DuplicatedObjectException{
        /**
         * The entity customer has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method checked if the customer that we want to modify, have same email of another existent
         * customer and in this case raises a DuplicatedObjectException, therefore change attributes in USER & CUSTOMER
         * tables.
         *
         * @return Return the object updated correctly in the DB otherwise raise an exception.
         * */

        boolean exist; /* flag per sapere se esiste già un cliente con gli stessi dati */
        /*CON TALE QUERY CONTROLLO SE IL CLIENTE ESISTE */

        query
                = " SELECT C.ID"
                + " FROM CUSTOMER C JOIN USER U ON C.ID = U.ID"
                + " WHERE DELETED = 0 AND EMAIL = ? AND C.ID <> ?;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, customer.getUser().getEmail());
            ps.setLong(i++, customer.getId());

        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro cliente con gli stessi dati!!!*/
        } catch (SQLException e) {
            System.err.println("Errore nella exist = rs.next();");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        if (exist) {
            /*NON È UN ERRORE BLOCCANTE ==> TODO: deve essere gestito a livello di controller dando un messaggio di errore all'utente*/
            throw new DuplicatedObjectException("CustomerDAOJDBCImpl.update: Tentativo di aggiornamento di un cliente con dati di un altro cliente già esistente.");
        }

        /*Se non è stata sollevata alcuna eccezione, allora possiamo aggiornare i dati di cliente + utente associato*/
        query
                = " UPDATE CUSTOMER C"
                + " JOIN USER U ON C.ID = U.ID"
                + " SET "
                + "  NUM_BOOKED_RESERVATIONS = ?,"
                + "  NUM_ORDERED_PRODUCT = ?,"
                + "  BLOCKED = ?,"
                + "  EMAIL = ?," /*field della tabella USER*/
                + "  NAME = ?," /*field della tabella USER*/
                + "  SURNAME = ?," /*field della tabella USER*/
                + "  ADDRESS = ?," /*field della tabella USER*/
                + "  PHONE = ?," /*field della tabella USER*/
                + "  PASSWORD = ?," /*field della tabella USER*/
                + "  IS_ADMIN = ?," /*field della tabella USER*/
                + "  IS_EMPLOYEE = ?," /*field della tabella USER*/
                + "  IS_CUSTOMER = ?" /*field della tabella USER*/
                + " WHERE"
                + "  C.ID = ?;";


        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, customer.getNumBookedReservations()); /*field della tabella CUSTOMER*/
            ps.setInt(i++, customer.getNumOrderedProduct()); /*field della tabella CUSTOMER*/
            ps.setBoolean(i++, customer.getBlocked()); /*field della tabella CUSTOMER*/
            ps.setString(i++, customer.getUser().getEmail()); /*field della tabella USER*/
            ps.setString(i++, customer.getUser().getName()); /*field della tabella USER*/
            ps.setString(i++, customer.getUser().getSurname()); /*field della tabella USER*/
            ps.setString(i++, customer.getUser().getAddress()); /*field della tabella USER*/
            ps.setString(i++, customer.getUser().getPhone()); /*field della tabella USER*/
            ps.setString(i++, customer.getUser().getPassword()); /*field della tabella USER*/
            ps.setBoolean(i++, customer.getUser().isAdmin()); /*field della tabella USER*/
            ps.setBoolean(i++, customer.getUser().isEmployee()); /*field della tabella USER*/
            ps.setBoolean(i++, customer.getUser().isCustomer()); /*field della tabella USER*/
            ps.setLong(i++, customer.getId()); /*field della tabella CUSTOMER*/

        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }

        /*Chiudo il preparedStatement*/
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /* se non è stata sollevata alcuna eccezione fin qui, ritorno true perché significa
         * che l'aggiornamento di CUSTOMER & USER è andato a buon fine */
        return true;
    }

    private Customer readCustomer(ResultSet rs) {
        Customer customer = new Customer();

        try {
            customer.setId(rs.getLong("C.ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella customer.setID(rs.getLong(\"C.ID\")");
            throw new RuntimeException(e);
        }
        try {
            customer.setNumBookedReservations(rs.getInt("C.NUM_BOOKED_RESERVATIONS"));
        } catch (SQLException e) {
            System.err.println("Errore nella customer.setNumBookedReservations(rs.getInt(\"C.NUM_BOOKED_RESERVATIONS\")");
            throw new RuntimeException(e);
        }
        try {
            customer.setNumOrderedProduct(rs.getInt("C.NUM_ORDERED_PRODUCT"));
        } catch (SQLException e) {
            System.err.println("Errore nella customer.setNumOrderedProduct(rs.getInt(\"C.NUM_ORDERED_PRODUCT\")");
            throw new RuntimeException(e);
        }
        try {
            customer.setBlocked(rs.getBoolean("C.BLOCKED"));
        } catch (SQLException e) {
            System.err.println("Errore nella customer.setBoolean(rs.getLong(\"C.BLOCKED\")");
            throw new RuntimeException(e);
        }

        return customer;
    }

    private Customer readCustomerWithUserFields(ResultSet rs) {
        /**
         * In this case the result set contains the result of a JOIN between customer and user therefore it is possible
         * to take the user data and save it in the >User user< object inside customer object
         *
         * @return Customer object read from result set.
         */

        Customer customer = new Customer();
        User user = new User();

        /* Inserisco l'oggetto User all'interno dell'oggetto customer che sto leggendo */
        customer.setUser(user);

        /* Setto prima i dati provenienti dalla tabella USER */

        try {
            customer.getUser().setId(rs.getLong("U.ID")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setId(rs.getLong(\"U.ID\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setName(rs.getString("NAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setSurname(rs.getString("SURNAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setAddress(rs.getString("ADDRESS")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setAddress(rs.getString(\"ADDRESS\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setPhone(rs.getString("PHONE")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setEmail(rs.getString("EMAIL")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            customer.getUser().setPassword(rs.getString("PASSWORD")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella customer.getUser().setPassword(rs.getString(\"PASSWORD\"));");
            throw new RuntimeException(e);
        }
        /* I CAMPI IS_ADMIN, IS_EMPLOYEE, IS_CUSTOMER, DELETED sono stati tralasciati volontariamente */

        /*              FINE DATI TABELLA USER                */
        /*              INIZIO DATI TABELLA CUSTOMER          */
        try {
            customer.setId(rs.getLong("C.ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"C.ID\")");
            throw new RuntimeException(e);
        }

        try {
            customer.setNumBookedReservations(rs.getInt("NUM_BOOKED_RESERVATIONS"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getInt(\"NUM_BOOKED_RESERVATIONS\")");
            throw new RuntimeException(e);
        }

        try {
            customer.setNumOrderedProduct(rs.getInt("NUM_ORDERED_PRODUCT"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getInt(\"NUM_ORDERED_PRODUCT\")");
            throw new RuntimeException(e);
        }

        try {
            customer.setBlocked(rs.getBoolean("BLOCKED"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"BLOCKED\")");
            throw new RuntimeException(e);
        }

        return customer;

    }


}
