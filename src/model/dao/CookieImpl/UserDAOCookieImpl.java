package model.dao.CookieImpl;

import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Structure;
import model.mo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;


public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public User insert(Long id, Structure structure, String email, String name, String surname, String address, String phone, String password, LocalDate birthDate, String fiscalCode, char type) throws DuplicatedObjectException {
        /**
         * Create cookie with info about logged user and add cookie to response.
         *
         * @return The user actually logged.
         * */

        User loggedUser = new User();
        loggedUser.setId(id);
        loggedUser.setEmail(email);
        loggedUser.setName(name);
        loggedUser.setSurname(surname);
        loggedUser.setType(type);

        Cookie cookie = null;
        cookie = new Cookie("loggedUser", encode(loggedUser)); /* faccio l'encoding del cookie */
        cookie.setPath("/"); /* setto il path di validità del cookie all'interno dell'applicativo */
        response.addCookie(cookie); /* aggiungo il cookie appena creato alla response*/

        return loggedUser;
    }

    @Override
    public boolean update(User loggedUser) {

        /**
         * Create new cookie that will be added to existent response and overwrite existent cookie.
         * */
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return true;
    }

    @Override
    public boolean delete(User loggedUser) {
        /**
         * Set max-age ok cookie to 0, to delete cookie.
         *
         * @return true if everything is fine
         */

        /* ritorna true solo perché avendo l'interfaccia comune con UserDAOMySQLJDBCImpl
         * nel metodo delete, mi serviva ritornare true quando cancellavo un utente dal db
         * Stesso discorso per il parametro loggedUser, non serve a nulla ma è necessario metterlo per poter
         * rispettare l'interfaccia scritta nello UserDAO ==> boolean delete(User user);
         * */

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return true;
    }

    @Override
    public User findByEmail(String email) {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public ArrayList<User> fetchAllOnType(char userType) {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public boolean blockCustomer(User user) throws UnsupportedOperationException {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public boolean unBlockCustomer(User user) throws UnsupportedOperationException {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public ArrayList<User> findEmployeesByString(String searchString) {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public User findById(Long id) {
        /**
         * This operation is allowed only in UserDAOMySQLJDBCImpl, not here.
         * */
        throw new UnsupportedOperationException("Not supported for cookie. Only DB");
    }

    @Override
    public User findLoggedUser() {

        /**
         * Search the cookie for the name "loggedUser" and decode it.
         *
         * @return Logged user.
         * */
        Cookie[] cookies = request.getCookies(); /* prende i cookie passati dal browser */
        User loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    @Override
    public boolean isValid(User userFromDB) {
        /**
         * Analyze cookie comparing with current data into the Database
         * @return true if validity check passed correctly otherwise return false
         * */
        /* si poteva differenziare in base al tipo di utente, ma abbiamo scelto di non farlo... */

        if (userFromDB != null) {
            /* utente presente sul database */
            /* se è cancellato O è bloccato O entrambi devo ritornare false */
            if (userFromDB.isDeleted())
                return false;
            else if (userFromDB.isBlocked())
                return false;
            else
                return true;
        }
        return false;
    }


    private String encode(User loggedUser) {

        /**
         * Encode the parameter object into a string of the type 'value1#value2#...'
         */
        String encodedLoggedUser;
        encodedLoggedUser
                = loggedUser.getId() + "#"
                + loggedUser.getEmail() + "#"
                + loggedUser.getName() + "#"
                + loggedUser.getSurname() + "#"
                + loggedUser.getType() + "#";

        return encodedLoggedUser;

    }

    private User decode(String encodedLoggedUser) {
        /**
         * Decode into an object, the parameter string of the type 'value1#value2#...'
         */
        User loggedUser = new User();
        int i = 0;

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setId(Long.parseLong(values[i++]));
        loggedUser.setEmail(values[i++]);
        loggedUser.setName(values[i++]);
        loggedUser.setSurname(values[i++]);
        loggedUser.setType(values[i++].charAt(0));

        return loggedUser;

    }

}

