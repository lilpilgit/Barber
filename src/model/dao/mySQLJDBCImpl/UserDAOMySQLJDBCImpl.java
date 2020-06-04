package model.dao.mySQLJDBCImpl;

import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.ExtendedProduct;
import model.mo.Structure;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    private final String COUNTER_ID = "userId";
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public UserDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User insert(Long id, Structure structure, String email, String name, String surname, String address,
                       String phone, String password, LocalDate birthDate, String fiscalCode, char type) throws DuplicatedObjectException {
        /**
         * This method allows you to indifferently insert a user of type Admin, Customer or Employee.
         * @params id parameter is DUMMY for DB insert but it's necessary when cookie is created ( in fact the method signature is the same)
         * @return Returns the user inserted correctly in the DB otherwise raises an exception
         * */

        /*Setto l'oggetto di cui andrò a verificarne l'esistenza prima di inserirlo nel DB*/
        User user = new User();
        user.setStructure(structure);
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setPassword(password);
        user.setBirthDate(birthDate);
        user.setFiscalCode(fiscalCode);
        user.setType(type);
        user.setBlocked(false); /* sto inserendo un nuovo utente */
        user.setDeleted(false); /* sto inserendo un nuovo utente */
        Long newId = null;



        /*CON TALE QUERY CONTROLLO SE LO USER ESISTE GIÀ ALL'INTERNO DEL DB
         * 2 UTENTI CON LA STESSA EMAIL NON POSSONO ESISTERE PERTANTO CONTROLLO IL CAMPO MAIL*/
        query =
                "SELECT ID"
                        + " FROM USER"
                        + " WHERE EMAIL = ? OR FISCAL_CODE = ?;";

        System.err.println("EMAIL =>>" + user.getEmail());
        System.err.println("FISCAL_CODE =>>" + user.getFiscalCode());
        System.err.println("TYPE =>>" + user.getType());
        System.err.println("NAME =>>" + user.getName());
        System.err.println("SURNAME =>>" + user.getSurname());
        System.err.println("id =>>" + user.getId());
        System.err.println("PHONE =>>" + user.getPhone());
        System.err.println("ADDRESS =>>" + user.getAddress());
        System.err.println("PASSWORD =>>" + user.getPassword());
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, user.getEmail());
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

        boolean exist; /* flag per sapere se esiste o meno già quell'utente */
        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro utente!!!*/
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
            throw new DuplicatedObjectException("UserDAOJDBCImpl.insert: Tentativo di inserimento di un utente già esistente con email: {" + user.getEmail() + "}.");
        }

        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO IMPIEGATO DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER IMPIEGATI DIVERSI SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/
        query =
                "UPDATE COUNTER"
                        + " SET VALUE = VALUE + 1"
                        + " WHERE ID = ?";

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
            /* IL resultSet una volta letto l'id non serve più in quanto rimane da fare solo l'INSERT di USER e poi EMPLOYEE*/
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        /* L'unico campo dell'utente che rimane da settare è l'ID. */
        user.setId(newId);


        query = "INSERT INTO USER(ID, ID_STRUCTURE, EMAIL, NAME, SURNAME, ADDRESS, PHONE, PASSWORD, BIRTH_DATE, FISCAL_CODE, TYPE, BLOCKED, DELETED) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setLong(i++, user.getStructure().getId());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getName());
            ps.setString(i++, user.getSurname());
            ps.setString(i++, user.getAddress());
            ps.setString(i++, user.getPhone());
            ps.setString(i++, user.getPassword());
            ps.setDate(i++, Date.valueOf(user.getBirthDate()));
            ps.setString(i++, user.getFiscalCode());
            ps.setString(i++, String.valueOf(user.getType()));
            ps.setBoolean(i++, user.isBlocked());
            ps.setBoolean(i++, user.isDeleted());
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate()");
            throw new RuntimeException(e);
        }

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /*
         * Se non è stata sollevata alcuna eccezione fin qui, ritorno correttamente l'oggetto di classe User
         * appena inserito
         * */

        return user;
    }

    @Override
    public boolean update(User user) throws DuplicatedObjectException {
        /**
         * Update data about a user.
         *
         * @return Return the object updated correctly in the DB otherwise raise an exception.
         * */

        boolean exist; /* flag per sapere se esiste già un'utente con gli stessi dati */
        /* CON TALE QUERY CONTROLLO SE L'UTENTE ESISTE GIÀ CON LO STESSA EMAIL O CON LO STESSO CODICE FISCALE O ENTRAMBI */

        query =
                "SELECT ID "
                        + "FROM USER "
                        + "WHERE DELETED = 0 AND EMAIL = ? AND ID <> ? "
                        + "UNION "
                        + "SELECT ID "
                        + "FROM USER "
                        + "WHERE DELETED = 0 AND FISCAL_CODE = ? AND ID <> ?;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, user.getEmail());
            ps.setLong(i++, user.getId());
            ps.setString(i++, user.getFiscalCode());
            ps.setLong(i++, user.getId());

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
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro utente con gli stessi dati!!!*/
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
            throw new DuplicatedObjectException("UserDAOJDBCImpl.update: Tentativo di aggiornamento di un utente già esistente con email{" + user.getEmail() + "}.}.");
        }
        /*Se non è stata sollevata alcuna eccezione, allora possiamo aggiornare i dati di utente */

        boolean yesAdditionalQuery = false;
        yesAdditionalQuery = (user.getType() != 'C');

        String additionalQuery = (yesAdditionalQuery) ? "ID_STRUCTURE = ?," : "";

        query
                = " UPDATE USER"
                + " SET "
                + additionalQuery
                + "  EMAIL = ?,"
                + "  NAME = ?,"
                + "  SURNAME = ?,"
                + "  ADDRESS = ?,"
                + "  PHONE = ?,"
                + "  PASSWORD = ?,"
                + "  BIRTH_DATE = ?,"
                + "  FISCAL_CODE = ?,"
                + "  TYPE = ?,"
                + "  BLOCKED = ?,"
                + "  DELETED = ?"
                + " WHERE"
                + "  ID = ?;";


        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            if (yesAdditionalQuery)
                ps.setLong(i++, user.getStructure().getId()); /* un cliente non ha una struttura di riferimento */
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getName());
            ps.setString(i++, user.getSurname());
            ps.setString(i++, user.getAddress());
            ps.setString(i++, user.getPhone());
            ps.setString(i++, user.getPassword());
            ps.setDate(i++, (user.getBirthDate() != null) ? Date.valueOf(user.getBirthDate()) : null);
            ps.setString(i++, user.getFiscalCode());
            ps.setString(i++, String.valueOf(user.getType()));
            ps.setBoolean(i++, user.isBlocked());
            ps.setBoolean(i++, user.isDeleted());
            ps.setLong(i++, user.getId());

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
         * che l'aggiornamento di USER è andato a buon fine */
        return true;
    }

    @Override
    public User findById(Long id) {
        User user = new User();

        query =
                "SELECT *"
                        + " FROM USER"
                        + " WHERE ID = ? AND DELETED = 0;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, id);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query);");
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
                /*Se true significa che esiste un utente con quell'ID*/
                user = readUser(rs);
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

        return user;

    }

    @Override
    public boolean delete(User user) {
        /**
         * Flag by DELETED column a USER as deleted indifferently if is Admin,Customer or Employee
         *
         * @return true if delete go correctly otherwise raise exception
         */
        query
                = "UPDATE USER"
                + " SET DELETED = '1'"
                + " WHERE ID = ?";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, user.getId());
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
    public User findByEmail(String email) {
        /**
         * Search user in table USER by Email that is UNIQUE.
         *
         * @return object User found if exist otherwise raise exception
         */

        User user = null;

        query =
                "SELECT *"
                        + " FROM USER"
                        + " WHERE EMAIL = ? AND DELETED = 0;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, email);
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery();");
            throw new RuntimeException(e);
        }
        try {
            if (rs.next()) {
                user = readUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User findLoggedUser() {
        /**
         * This operation is allowed only in UserDAOCookieImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for DB. Only cookie");
    }

    @Override
    public ArrayList<User> fetchAllOnType(char userType) {
        /**
         * Fetch all user with type = userType in USER table.
         *
         * @params char userType : a char in list {A,C,E}
         * @return Return the ArrayList<User> with all user that are employee.
         * otherwise raise an exception.
         * */
        ArrayList<User> users = new ArrayList<>();
        /* Selezioni solamente gli utenti che non sono stati cancellati */
        query =
                "SELECT *"
                        + " FROM USER"
                        + " WHERE DELETED = 0 AND TYPE = ?;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setString(i++, String.valueOf(userType));
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
            while (rs.next()) { /* Fin tanto che esiste un utente di tipo userType */
                users.add(readUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella users.add(readUser(rs));");
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

        return users;
    }

    @Override
    public boolean blockCustomer(User user) throws UnsupportedOperationException {
        /**
         * Call this method if and only if on user object with type = 'C'.
         *
         * @return true if user (customer) is blocked correctly otherwise raise an exception.
         */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile bloccare un utente che non è un cliente. Errore con l'utente con id{" + user.getId() + "}.");

        query =
                "UPDATE USER"
                        + " SET BLOCKED = '1'"
                        + " WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, user.getId());
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
    public boolean unBlockCustomer(User user) throws UnsupportedOperationException {
        /**
         * Call this method if and only if on user object with type = 'C'.
         *
         * @return true if user (customer) is unblocked correctly otherwise raise an exception.
         */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile sbloccare un utente che non è un cliente. Errore con l'utente con id{" + user.getId() + "}.");


        query =
                "UPDATE USER"
                        + " SET BLOCKED = '0'"
                        + " WHERE ID = ?";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, user.getId());
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

    /* TODO: metodi riguardanti operazioni nel carrello... DA SPOSTARE IN UN ALTRO DAO? */

    public ArrayList<ExtendedProduct> fetchCart(User user) throws UnsupportedOperationException {
        /**
         * Fetch ArrayList<ExtendendProduct> that is Cart of user passed as parameter.
         *
         * @params User user : a user with type {C}
         * @return Return the ArrayList<ExtendedProduct> for the user passed as parameter
         * otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile caricare il carrello di un utente che non è cliente. Errore con l'utente con id{" + user.getId() + "}.");

        ArrayList<ExtendedProduct> cart = new ArrayList<>();
        /* Seleziono i prodotti presenti nel carrello dell'utente passato come parametro */
        query =
                "SELECT ID, PRODUCER, PRICE, DISCOUNT, NAME, PIC_NAME, DESCRIPTION, QUANTITY, CATEGORY, QUANTITY, DESIRED_QTY "
                        + "FROM CART C INNER JOIN PRODUCT P ON C.ID_PRODUCT = P.ID "
                        + "WHERE C.ID_CUSTOMER = ? AND P.DELETED = 0;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
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
            while (rs.next()) { /* Fin tanto che esiste un prodotto nel carrello di tipo ExtendedProduct */
                cart.add(readCart(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella cart.add(readCartProduct(rs));");
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

        return cart;

    }

    public boolean addProductToCart(User user, Long idProduct, Integer desiredQty) throws UnsupportedOperationException {
        /**
         * Add product to cart of user logged.
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to add to cart of user.
         *         Integer desiredQty : desired quantity to add to the cart
         * @return true if product is successfully added to cart otherwise raise an exception.
         * */

        ExtendedProduct productInCart = null; /* mappa la tupla del prodotto già esistente nel carrello dell'utente */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} al carrello dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query =
                "SELECT * "
                        + "FROM CART "
                        + "WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setLong(i++, idProduct);
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
            if (rs.next()) {
                /*Se true significa che esiste già tale prodotto nel carrello dell'utente dunque va aggiornata solamente la DESIRED_QTY */
                query =
                        "UPDATE CART "
                                + "SET DESIRED_QTY = DESIRED_QTY + ? "
                                + "WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

                try {
                    int i = 1;
                    ps = connection.prepareStatement(query);
                    ps.setInt(i++, desiredQty);
                    ps.setLong(i++, user.getId());
                    ps.setLong(i++, idProduct);
                } catch (SQLException e) {
                    System.err.println("Errore nella ps = connection.prepareStatement(query)");
                    throw new RuntimeException(e);
                }
                try {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Errore nella ps.executeUpdate();");
                    throw new RuntimeException(e);
                }

            } else {
                /* il prodotto non esiste ancora nel carrello dell'utente pertanto bisogna aggiungerlo */
                query = "INSERT INTO CART(ID_CUSTOMER, ID_PRODUCT, DESIRED_QTY) VALUES(?,?,?);";
                try {
                    int i = 1;
                    ps = connection.prepareStatement(query);
                    ps.setLong(i++, user.getId());
                    ps.setLong(i++, idProduct);
                    ps.setInt(i++, desiredQty);
                } catch (SQLException e) {
                    System.err.println("Errore nella ps = connection.prepareStatement(query)");
                    throw new RuntimeException(e);
                }
                try {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Errore nella ps.executeUpdate();");
                    throw new RuntimeException(e);
                }

            }
        } catch (SQLException e) {
            System.err.println("Errore nella if(rs.next())");
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

        return true;
    }

    public boolean removeProductFromCart(User user, Long idProduct) throws UnsupportedOperationException {
        /**
         * Remove product from cart of logged user .
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to remove from cart of user.
         *
         * @return true if product is successfully removed from cart otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} al carrello dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query = "DELETE FROM CART WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setLong(i++, idProduct);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeUpdate();");
            throw new RuntimeException(e);
        }
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        return true;
    }


    /* ******************************************************************************** */

    private User readUser(ResultSet rs) {
        /**
         * Read user attributes from query on single table USER
         *
         * @return User object read from result set.
         */
        User user = new User();
        Structure structure = new Structure();
        user.setStructure(structure);

        try {/* deve stare per primo per poter sapere che tipo di utente stiamo trattando */
            user.setType(rs.getString("TYPE").charAt(0));
        } catch (
                SQLException e) {
            System.err.println("Errore nella user.setType(rs.getString(\"TYPE\").charAt(0));");
            throw new RuntimeException(e);
        }
        try {
            user.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setId(rs.getLong(\"ID\"));");
            throw new RuntimeException(e);
        }

        try {
            user.getStructure().setId(rs.getLong("ID_STRUCTURE"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setId(rs.getLong(\"ID_STRUCTURE\"));");
            throw new RuntimeException(e);
        }

        try {
            user.setEmail(rs.getString("EMAIL"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setSurname(rs.getString("SURNAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setAddress(rs.getString("ADDRESS"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setAddress(rs.getString(\"ADDRESS\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setPhone(rs.getString("PHONE"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setPhone(rs.getString(\"PHONE\"));;");
            throw new RuntimeException(e);
        }
        try {
            user.setPassword(rs.getString("PASSWORD"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setPassword(rs.getString(\"PASSWORD\"));;");
            throw new RuntimeException(e);
        }
        try {
            user.setBirthDate(rs.getObject("BIRTH_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setBirthDate(rs.getObject(\"BIRTH_DATE\", LocalDate.class));");
            throw new RuntimeException(e);
        }
        try {
            user.setFiscalCode(rs.getString("FISCAL_CODE"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella user.setFiscalCode(rs.getString(\"FISCAL_CODE\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setBlocked(rs.getBoolean("BLOCKED"));
        } catch (SQLException e) {
            System.err.println("Errore nella user.setBlocked(rs.getBoolean(\"BLOCKED\"));");
            throw new RuntimeException(e);
        }
        try {
            user.setDeleted(rs.getBoolean("DELETED"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella user.setDeleted(rs.getBoolean(\"DELETED\"));");
            throw new RuntimeException(e);
        }

        return user;
    }

    private ExtendedProduct readCart(ResultSet rs) {
        /**
         * Read an ExtendedProduct attributes
         *
         * @return ExtendedProduct object read from result set.
         */

        ExtendedProduct extendedProduct = new ExtendedProduct();

        try {
            extendedProduct.setId(rs.getLong("ID"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella extendedProduct.setId(rs.getLong(\"ID\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setProducer(rs.getString("PRODUCER"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella extendedProduct.setProducer(rs.getString(\"PRODUCER\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPrice(rs.getBigDecimal("PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setPrice(rs.getBigDecimal(\"PRICE\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDiscount(rs.getInt("DISCOUNT"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setDiscount(rs.getInt(\"DISCOUNT\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPictureName(rs.getString("PIC_NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setPictureName(rs.getString(\"PIC_NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDescription(rs.getString("DESCRIPTION"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setDescription(rs.getString(\"DESCRIPTION\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setCategory(rs.getString("CATEGORY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setCategory(rs.getString(\"CATEGORY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setQuantity(rs.getInt("QUANTITY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setQuantity(rs.getInt(\"QUANTITY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setRequiredQuantity(rs.getInt("DESIRED_QTY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setRequiredQuantity(rs.getInt(\"DESIRED_QTY\"));");
            throw new RuntimeException(e);
        }

        return extendedProduct;
    }
}
