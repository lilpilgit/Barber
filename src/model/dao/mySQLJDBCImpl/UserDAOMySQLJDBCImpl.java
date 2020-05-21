package model.dao.mySQLJDBCImpl;

import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.User;

import java.sql.*;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public UserDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User insert(Long id, String email, String name, String surname, String address,
                       String phone, String password, Boolean isAdmin, Boolean isEmployee,
                       Boolean isCustomer) throws DuplicatedObjectException {
        /**
         * WARNING! This method must NEVER be called directly inside the controller,
         * but the insert methods of EmployeeDAOMySQLJDCImpl and UserDAOMySQLJDBCImpl will implicitly call it.
         * @return Returns the user inserted correctly in the DB otherwise raises an exception
         * */

        /*Setto l'oggetto di cui andrò a verificarne l'esistenza prima di inserirlo nel DB*/
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setPassword(password);
        user.setIsAdmin(isAdmin);
        user.setIsEmployee(isEmployee);
        user.setIsCustomer(isCustomer);


        /*CON TALE QUERY CONTROLLO SE LO USER ESISTE GIÀ ALL'INTERNO DEL DB
         * 2 UTENTI CON LA STESSA EMAIL NON POSSONO ESISTERE PERTANTO CONTROLLO IL CAMPO MAIL*/
        query = "SELECT ID FROM USER WHERE EMAIL = ?;";
        System.err.println("EMAIL =>>" +  user.getEmail());
        System.err.println("NAME =>>" +  user.getName());
        System.err.println("SURNAME =>>" +  user.getSurname());
        System.err.println("id =>>" +  user.getId());
        System.err.println("PHONE =>>" +  user.getPhone());
        System.err.println("ADDRESS =>>" +  user.getAddress());
        System.err.println("PASSWORD =>>" +  user.getPassword());
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


        query = "INSERT INTO USER(ID, EMAIL, NAME, SURNAME, ADDRESS, PHONE, PASSWORD, IS_ADMIN, IS_EMPLOYEE, IS_CUSTOMER, DELETED) VALUES(?,?,?,?,?,?,?,?,?,?,?);";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getName());
            ps.setString(i++, user.getSurname());
            ps.setString(i++, user.getAddress());
            ps.setString(i++, user.getPhone());
            ps.setString(i++, user.getPassword());
            ps.setBoolean(i++, user.isAdmin());
            ps.setBoolean(i++, user.isEmployee());
            ps.setBoolean(i++, user.isCustomer());
            ps.setBoolean(i++, false); /*VALORE PER DELETED HARDCODED A false IN QUANTO SIAMO NEL METODO insert DI USER*/
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

        return user;
    }

}
