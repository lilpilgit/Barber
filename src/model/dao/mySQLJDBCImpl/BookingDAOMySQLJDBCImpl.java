package model.dao.mySQLJDBCImpl;

import model.dao.BookingDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Booking;
import model.mo.Structure;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class BookingDAOMySQLJDBCImpl implements BookingDAO {

    final private String COUNTER_ID = "bookingId";
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public BookingDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Booking insert(LocalDate date, Time hourStart, User customer, Structure structure) throws DuplicatedObjectException {

        /* Se deleted e' null la prenotazione e' valida (NON VIENE SETTATA LA isDeleted cosi' e' null di default)
        , se 1 e' cancellata dall'utente, se 0 e' cancellata dall'amministratore*/

        /*Setto l'oggetto di cui andrò a verificarne l'esistenza prima di inserirlo nel DB*/
        Booking booking = new Booking();
        booking.setDeletedReason(null);
        booking.setDate(date);
        booking.setHourStart(hourStart);
        booking.setCustomer(customer);
        booking.setStructure(structure);
        Long newId = null;


        /* CON TALE QUERY CONTROLLO SE ESISTE GIA' UN APPUNTAMENTO PER LA STESSA DATA E LA STESSA ORA */

        query = "SELECT * FROM BOOKING WHERE DATE = ? AND HOUR_START = ? AND DELETED IS NULL;";

        System.err.println("DATE =>>" + booking.getDate());
        System.err.println("HOUR_START =>>" + booking.getHourStart());

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setDate(i++, java.sql.Date.valueOf(booking.getDate()));
            ps.setTime(i++, booking.getHourStart());

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

        boolean exist; /* flag per sapere se esiste o meno già l'appuntamento */
        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire l'appuntamento!!!*/
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
            /*NON È UN ERRORE BLOCCANTE*/
            throw new DuplicatedObjectException("BookingDAOJDBCImpl.insert: Tentativo di inserimento di " +
                    "un appuntamento già esistente con data: {" + booking.getDate() + "} e ora {" + booking.getHourStart() + "}");
        }

        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO APPUNTAMENTO DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER PRENOTAZIONI DIVERSE SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/
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
            /* IL resultSet una volta letto l'id non serve più */
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        booking.setId(newId);

        query = "INSERT INTO BOOKING(ID, DELETED_REASON, DATE, HOUR_START, ID_CUSTOMER, ID_STRUCTURE) VALUES (?,?,?,?,?,?);";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, booking.getId());
            ps.setString(i++, booking.getDeletedReason());
            ps.setDate(i++, Date.valueOf(booking.getDate()));
            ps.setTime(i++, booking.getHourStart());
            ps.setLong(i++, booking.getCustomer().getId());
            ps.setLong(i++, booking.getStructure().getId());
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
        return booking;
    }

    @Override
    public boolean deleteBooking(Booking booking) {
        /**
         * If you are userType 'A'
         * Flag with 0 DELETED column in USER table and set DELETED_REASON
         * If you are userType 'C'
         * Flag with 1 DELETED column in USER table and set DELETED_REASON
         *
         * @return true if delete go correctly otherwise raise exception
         */
        query
                = "UPDATE BOOKING"
                + " SET DELETED = ?, "
                + "     DELETED_REASON = ?"
                + "WHERE ID = ?";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setBoolean(i++, booking.isDeleted());
            ps.setString(i++, booking.getDeletedReason());
            ps.setLong(i++, booking.getId());
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
    public ArrayList<Booking> findBookingsByDate(LocalDate date) {
        /**
         * Fetch all bookings to show in customer area by specified date
         */
        ArrayList<Booking> bookings = new ArrayList<>();
        query = "SELECT * FROM BOOKING WHERE DATE = ? AND DELETED IS NULL ORDER BY HOUR_START ASC;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setDate(i++, Date.valueOf(date));
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeQuery()");
            throw new RuntimeException(e);
        }
        try {
            while (rs.next()) {
                bookings.add(readBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
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

        return bookings;
    }

    @Override
    public Booking findBookingById(Long id) {
        /**
         * Fetch only booking with DELETED == NULL, i.e. which has not been canceled by admin or customer
         * */
        Booking booking = new Booking();
        String query = "SELECT * FROM BOOKING WHERE ID = ? AND DELETED IS NULL;"; /* trova solo le prenotazioni non cancellate sia da admin che da customer*/
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
            System.err.println("Errore nella ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            if (rs.next()) { //the element with this id is present
                booking = readBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
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
        return booking;
    }

    @Override
    public Booking getLastBooking(Long idCustomer, Long idStructure) {
        /**
         * Fetch only the last appointment that has not been canceled by the client
         * */

        Booking booking = null;
        /* Trova solo l'ultima prenotazione che e' stata effettuata. Inoltre, la prenotazione si
        * deve trovare in una data futura rispetto alla CURDATE() */

        String query = "SELECT * FROM BOOKING WHERE DATE >= CURDATE() AND ID_CUSTOMER = ? " +
                "AND ID_STRUCTURE = ? ORDER BY ID DESC LIMIT 1;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, idCustomer);
            ps.setLong(i++, idStructure);
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeQuery()");
            throw new RuntimeException(e);
        }
        try {
            if (rs.next()) { //the element with this id is present
                booking = readBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
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
        return booking;
    }

    @Override
    public ArrayList<Booking> findBookingsByDateForAdmin(LocalDate date) {
        /**
         * Fetch all bookings for admin area with customer attributes by JOIN
         */
        ArrayList<Booking> bookings = new ArrayList<>();
        query =
                "SELECT BOOKING.ID,HOUR_START,BOOKING.DELETED,BOOKING.DELETED_REASON,NAME,SURNAME,EMAIL,PHONE "
                        + "FROM BOOKING INNER JOIN USER ON BOOKING.ID_CUSTOMER = USER.ID "
                        + "WHERE DATE = ? "
                        + "ORDER BY HOUR_START ASC;";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setDate(i++, Date.valueOf(date));
        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.executeQuery()");
            throw new RuntimeException(e);
        }
        try {
            while (rs.next()) {
                bookings.add(readBookingForAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.next()");
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

        return bookings;
    }

    private Booking readBooking(ResultSet rs) {

        /**
         * In this method we set all booking's fields from table BOOKING
         */

        Booking booking = new Booking();
        Structure structure = new Structure();
        User customer = new User();
        booking.setStructure(structure);
        booking.setCustomer(customer);

        try {
            booking.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            boolean status = rs.getBoolean("DELETED");
            /* se è null significa che la prenotazione è ancora valida dunque lascio a null il wrapper Boolean deleted */
            if (!rs.wasNull()) {
                booking.setDeleted(status); /* metterò il valore true o false che sia */
            }
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDeletedReason(rs.getString("DELETED_REASON"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"DELETED_REASON\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDate(rs.getObject("DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getDate(\"DATE\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setHourStart(rs.getTime("HOUR_START"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getTime(\"HOUR_START\")");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setId(rs.getLong("ID_CUSTOMER"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_CUSTOMER\")");
            throw new RuntimeException(e);
        }
        try {
            booking.getStructure().setId(rs.getLong("ID_STRUCTURE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID_STRUCTURE\")");
            throw new RuntimeException(e);
        }

        return booking;
    }

    private Booking readBookingForAdmin(ResultSet rs) {

        /**
         * Read some booking's field with some useful information about customer correlated.
         */

        Booking booking = new Booking();
        Structure structure = new Structure();
        User customer = new User();
        booking.setStructure(structure);
        booking.setCustomer(customer);

        try {
            booking.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            boolean status = rs.getBoolean("DELETED");
            /* se è null significa che la prenotazione è ancora valida dunque lascio a null il wrapper Boolean deleted */
            if (!rs.wasNull()) {
                booking.setDeleted(status); /* metterò il valore true o false che sia */
            }

        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setDeletedReason(rs.getString("DELETED_REASON"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"DELETED_REASON\")");
            throw new RuntimeException(e);
        }
        try {
            booking.setHourStart(rs.getTime("HOUR_START"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getTime(\"HOUR_START\")");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella booking.getCustomer().setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setSurname(rs.getString("SURNAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella booking.getCustomer().setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setEmail(rs.getString("EMAIL"));
        } catch (SQLException e) {
            System.err.println("Errore nella booking.getCustomer().setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            booking.getCustomer().setPhone(rs.getString("PHONE"));
        } catch (SQLException e) {
            System.err.println("Errore nella booking.getCustomer().setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }

        return booking;
    }

}
