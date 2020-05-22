package model.dao.mySQLJDBCImpl;

import model.dao.AdminDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Admin;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;

public class AdminDAOMySQLJDBCImpl implements AdminDAO {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public AdminDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Admin findById(Long id) {
        Admin admin = new Admin();
        query = "SELECT * FROM ADMIN JOIN USER U ON ADMIN.ID = U.ID WHERE U.ID = ? AND DELETED = 0;";
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
                /*Se true significa che esiste un admin con quell'ID*/
                admin = readAdminWithUserFields(rs);
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

        return admin;
    }

    @Override
    public boolean update(Admin admin) throws DuplicatedObjectException {
        /**
         * The entity admin has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method checked if the admin that we want to modify, have same fiscal code of another existent
         * admin and in this case raises a NoEmployeeCreatedException, therefore change attributes in USER & ADMIN
         * tables.
         *
         * @return Return true if the object is updated correctly in the DB otherwise raise an exception.
         * */

        boolean exist; /* flag per sapere se esiste già un'admin con gli stessi dati */
        /*CON TALE QUERY CONTROLLO SE L'ADMIN ESISTE GIÀ */

        query
                = " SELECT ADMIN.ID"
                + " FROM ADMIN JOIN USER U ON ADMIN.ID = U.ID"
                + " WHERE DELETED = 0 AND FISCAL_CODE = ? AND ADMIN.ID <> ?;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, admin.getFiscalCode());
            ps.setLong(i++, admin.getId());

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
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro admin con gli stessi dati!!!*/
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
            throw new DuplicatedObjectException("AdminDAOJDBCImpl.update: Tentativo di aggiornamento di un admin già esistente.");
        }

        /*Se non è stata sollevata alcuna eccezione, allora possiamo aggiornare i dati di admin + utente associato*/
        query
                = " UPDATE ADMIN A"
                + " JOIN USER U ON A.ID = U.ID"
                + " SET "
                + "  BIRTH_DATE = ?,"
                + "  FISCAL_CODE = ?,"
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
                + "  A.ID = ?;";


        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setDate(i++, Date.valueOf(admin.getBirthDate())); /*field della tabella ADMIN*/
            ps.setString(i++, admin.getFiscalCode()); /*field della tabella ADMIN*/
            ps.setString(i++, admin.getUser().getEmail()); /*field della tabella USER*/
            ps.setString(i++, admin.getUser().getName()); /*field della tabella USER*/
            ps.setString(i++, admin.getUser().getSurname()); /*field della tabella USER*/
            ps.setString(i++, admin.getUser().getAddress()); /*field della tabella USER*/
            ps.setString(i++, admin.getUser().getPhone()); /*field della tabella USER*/
            ps.setString(i++, admin.getUser().getPassword()); /*field della tabella USER*/
            ps.setBoolean(i++, admin.getUser().isAdmin()); /*field della tabella USER*/
            ps.setBoolean(i++, admin.getUser().isEmployee()); /*field della tabella USER*/
            ps.setBoolean(i++, admin.getUser().isCustomer()); /*field della tabella USER*/
            ps.setLong(i++, admin.getId()); /*field della tabella ADMIN*/

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
         * che l'aggiornamento di ADMIN & USER è andato a buon fine */
        return true;
    }


    private Admin readAdminWithUserFields(ResultSet rs) {
        /**
         * In this case the result set contains the result of a JOIN between admin and user therefore it is possible
         * to take the user data and save it in the >User user< object inside admin object
         *
         * @return Admin object read from result set.
         */
        Admin admin = new Admin();
        User user = new User();

        /* Inserisco l'oggetto User all'interno dell'oggetto Admin che sto leggendo */
        admin.setUser(user);

        /* Setto prima i dati provenienti dalla tabella USER */
        try {
            admin.getUser().setId(rs.getLong("U.ID")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setId(rs.getLong(\"U.ID\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setName(rs.getString("NAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setSurname(rs.getString("SURNAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setAddress(rs.getString("ADDRESS")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setAddress(rs.getString(\"ADDRESS\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setPhone(rs.getString("PHONE")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setEmail(rs.getString("EMAIL")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setPassword(rs.getString("PASSWORD")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setPassword(rs.getString(\"PASSWORD\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setIsAdmin(rs.getBoolean("IS_ADMIN")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setIsAdmin(rs.getBoolean(\"IS_ADMIN\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setIsEmployee(rs.getBoolean("IS_EMPLOYEE")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setIsEmployee(rs.getBoolean(\"IS_EMPLOYEE\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setIsCustomer(rs.getBoolean("IS_CUSTOMER")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setIsCustomer(rs.getBoolean(\"IS_CUSTOMER\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.getUser().setIsDeleted(rs.getBoolean("DELETED")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella admin.getUser().setIsDeleted(rs.getBoolean(\"DELETED\"));");
            throw new RuntimeException(e);
        }
        /*              FINE DATI TABELLA USER                */
        /*              INIZIO DATI TABELLA EMPLOYEE          */
        try {
            admin.setId(rs.getLong("ADMIN.ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella admin.setId(rs.getLong(\"ADMIN.ID\"));");
            throw new RuntimeException(e);
        }
        try {
            admin.setBirthDate(rs.getObject("BIRTH_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella admin.setBirthDate(rs.getObject(\"BIRTH_DATE\", LocalDate.class));");
            throw new RuntimeException(e);
        }
        try {
            admin.setFiscalCode(rs.getString("FISCAL_CODE"));
        } catch (SQLException e) {
            System.err.println("Errore nella admin.setFiscalCode(rs.getString(\"FISCAL_CODE\"));");
            throw new RuntimeException(e);
        }

        return admin;

    }
}
