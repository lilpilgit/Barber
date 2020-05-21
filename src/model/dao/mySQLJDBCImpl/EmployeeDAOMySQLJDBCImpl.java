package model.dao.mySQLJDBCImpl;

import model.dao.EmployeeDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.exception.NoEmployeeCreatedException;
import model.mo.Employee;
import model.mo.Structure;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeDAOMySQLJDBCImpl implements EmployeeDAO {

    private final String COUNTER_ID = "userId"; /*L'employee non ha una propria entry nella table COUNTER in quanto il suo id è strettamente legato a quello dell'utente*/
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public EmployeeDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }


    /**/
    @Override
    public Employee insert(UserDAO userDAO,
                           LocalDate birthDate,
                           String fiscalCode,
                           LocalDate hireDate,
                           Structure structure,
                           String email, /* [+] attributo della table USER */
                           String name, /* [+] attributo della table USER */
                           String surname, /* [+] attributo della table USER */
                           String address, /* [+] attributo della table USER */
                           String phone, /* [+] attributo della table USER */
                           String password /* [+] attributo della table USER */)
            throws NoEmployeeCreatedException {
        /**
         * The entity employee has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method checked if the employee with the fiscal code passed as parameter (String fiscalCode)
         * already exists (in which case raises a NoEmployeeCreatedException), inserts the user with ID manually
         * increased on the COUNTER table and only if no exception is raised, inserts the <Employee employee> object
         * in the EMPLOYEE table.
         *
         * @return Return the object inserted correctly in the DB otherwise raise an exception.
         * */

        /*Setto l'oggetto di cui andrò a verificarne l'esistenza prima di inserirlo nel DB*/
        Employee employee = new Employee();
        employee.setBirthDate(birthDate);
        employee.setFiscalCode(fiscalCode);
        employee.setHireDate(hireDate);
        employee.setStructure(structure);


        boolean result; /* result of insert operation*/
        int affectedRows; /*number of rows affected by the insert*/
        ResultSet insertedRow;
        Long newId = null;

        /*CON TALE QUERY CONTROLLO SE L'IMPIEGATO ESISTE GIÀ ALL'INTERNO DELLA STRUTTURA SPECIFICATA NEL PARAMETRO*/
        query
                = " SELECT EMPLOYEE.ID "
                + " FROM EMPLOYEE INNER JOIN USER U ON EMPLOYEE.ID = U.ID "
                + " WHERE "
                + " DELETED = 0 AND "
                + " FISCAL_CODE = ? AND"
                + " ID_STRUCTURE = ?;";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, employee.getFiscalCode());
            ps.setLong(i++, employee.getStructure().getId());

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

        boolean exist; /* flag per sapere se esiste o meno già quell'impiegato */
        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro impiegato!!!*/
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
            throw new NoEmployeeCreatedException("EmployeeDAOJDBCImpl.insert: Tentativo di inserimento di un impiegato già esistente presso la struttura con ID: {" + employee.getStructure().getId() + "}.");
        }

        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO IMPIEGATO DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER IMPIEGATI DIVERSI SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/
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
            /* IL resultSet una volta letto l'id non serve più in quanto rimane da fare solo l'INSERT di USER e poi EMPLOYEE*/
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        /* L'unico campo dell'employee che rimane da settare è l'ID. Setto l'id dell'impiegato con il nuovo ID. */
        employee.setId(newId);

        /*              MOLTO IMPORTANTE !!!!!
            Viene prima aggiunto l'utente e se l'inserimento è andato a buon fine verrà aggiunto anche l'impiegato
            con lo stesso ID. Notare che viene usato lo userDAO istanziato nella transazione dal controller,
            perché deve essere uno userDAO istanziato all'interno della medesima transazione con il DB
         */

        try {
            User user = userDAO.insert(newId, email, name, surname, address, phone, password, false, true, false);
            /*                                                                                    ^^^^^^^^^^^^^^ ==> STIAMO INSERENDO UN UTENTE COME IMPIEGATO!!!*/
            /*Setto l'unico campo rimanente che risulta essere user all'interno dell'oggetto employee da inserire nel DB*/
            employee.setUser(user);

        } catch (DuplicatedObjectException e) {
            throw new NoEmployeeCreatedException("EmployeeDAOJDBCImpl.insert: Tentativo di inserimento di un impiegato già esistente con email : {" + email + "} presso la struttura con ID: {" + employee.getStructure().getId() + "}.");
        }

        /* Se non è stata sollevata alcuna eccezione fin qui, allora:

                >l'IMPIEGATO NON ESISTE GIÀ
                >lo USER NON ESISTE GIÀ CON TALE EMAIL

           Pertanto provo a inserirlo nel DB
        */

        query = "INSERT INTO EMPLOYEE(ID, BIRTH_DATE, FISCAL_CODE, HIRE_DATE,ID_STRUCTURE) VALUES(?,?,?,?,?);";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, employee.getId());
            ps.setDate(i++, Date.valueOf(employee.getBirthDate()));
            ps.setString(i++, employee.getFiscalCode());
            ps.setDate(i++, Date.valueOf(employee.getHireDate()));
            ps.setLong(i++, employee.getStructure().getId());
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
         * Se non è stata sollevata alcuna eccezione fin qui, ritorno correttamente l'oggetto di classe Employee
         * appena inserito
         * */

        return employee;
    }

    @Override
    public ArrayList<Employee> fetchAll() {
        /**
         * The entity employee has an ID that appears to be FOREIGN KEYS of the PRIMARY KEYS <ID> of the USER entity,
         * therefore this method extracts all data of all employee including those present in the user table by
         * performing a JOIN of the 2 tables on the ID field.
         *
         * @return Return the ArrayList<Employee> with all employee that are not deleted in the UNIQUE structure
         * otherwise raise an exception.
         * */
        ArrayList<Employee> employees = new ArrayList<>();
        /* Selezioni solamente gli impiegati che non sono stati cancellati */
        query = "SELECT * FROM EMPLOYEE INNER JOIN USER U ON EMPLOYEE.ID = U.ID WHERE DELETED = 0;";
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
            while (rs.next()) { /* Fin tanto che esiste un impiegato */
                employees.add(readEmployeeWithUserFields(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella employees.add(readEmployee(rs));");
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

        return employees;
    }

    @Override
    public Employee findById(Long id) {
        Employee employee = new Employee();
        query = "SELECT * FROM EMPLOYEE JOIN USER U ON EMPLOYEE.ID = U.ID WHERE U.ID = ? AND DELETED = 0;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++,id);
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
                /*Se true significa che esiste un impiegato con quell'ID*/
                employee = readEmployeeWithUserFields(rs);
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

        return employee;
    }

    private Employee readEmployeeWithUserFields(ResultSet rs) {
        /**
         * In this case the result set contains the result of a JOIN between employee and user therefore it is possible
         * to take the user data and save it in the >User user< object inside employee object
         *
         * @return Employee object read from result set.
         */
        Employee employee = new Employee();
        User user = new User();
        Structure structure = new Structure();

        /* Inserisco l'oggetto User e Structure all'interno dell'oggetto Employee che sto leggendo */
        employee.setUser(user);
        employee.setStructure(structure);

        /* Setto prima i dati provenienti dalla tabella USER */
        try {
            employee.getUser().setId(rs.getLong("U.ID")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setId(rs.getLong(\"U.ID\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setName(rs.getString("NAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setSurname(rs.getString("SURNAME")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setAddress(rs.getString("ADDRESS")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setAddress(rs.getString(\"ADDRESS\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setPhone(rs.getString("PHONE")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setEmail(rs.getString("EMAIL")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            employee.getUser().setPassword(rs.getString("PASSWORD")); /*  COLONNA DELLA TABELLA USER */
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setPassword(rs.getString(\"PASSWORD\"));");
            throw new RuntimeException(e);
        }
        /* I CAMPI IS_ADMIN, IS_EMPLOYEE, IS_CUSTOMER, DELETED sono stati tralasciati volontariamente */

        /*              FINE DATI TABELLA USER                */
        /*              INIZIO DATI TABELLA EMPLOYEE          */
        try {
            employee.setId(rs.getLong("EMPLOYEE.ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            employee.getStructure().setId(rs.getLong("ID_STRUCTURE")); /* Setto solamente l'ID della struttura che poi inserirò all'interno dell'oggetto employee*/
        } catch (SQLException e) {
            System.err.println("Errore nella employee.getUser().setId(rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            employee.setBirthDate(rs.getObject("BIRTH_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"BIRTH_DATE\",LocalDate.class)");
            throw new RuntimeException(e);
        }
        try {
            employee.setFiscalCode(rs.getString("FISCAL_CODE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"FISCAL_CODE\")");
            throw new RuntimeException(e);
        }
        try {
            employee.setHireDate(rs.getObject("HIRE_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"HIRE_DATE\", LocalDate.class)");
            throw new RuntimeException(e);
        }

        return employee;

    }

}
