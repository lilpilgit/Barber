package model.dao.mySQLJDBCImpl;

import model.dao.CustomerDAO;
import model.mo.Customer;
import model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        query = "SELECT * FROM CUSTOMER INNER JOIN USER U ON CUSTOMER.ID = U.ID WHERE DELETED = 0;";

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
        query = "SELECT * FROM CUSTOMER C WHERE C.ID = ?";
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
                customer = readCustomer(rs);
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

        query ="UPDATE CUSTOMER SET BLOCKED = '1' WHERE ID = ?";

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

        query ="UPDATE CUSTOMER SET BLOCKED = '0' WHERE ID = ?";

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
            customer.setId(rs.getLong("CUSTOMER.ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
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
