package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.ExtendedProduct;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface UserDAO {

    User insert(Long id, Structure structure, String email, String name, String surname, String address,
                String phone, String password, LocalDate birthDate, String fiscalCode, char type) throws DuplicatedObjectException;

    boolean update(User user) throws DuplicatedObjectException;

    User findById(Long id);

    boolean delete(User user);

    User findByEmail(String email);

    ArrayList<User> fetchAllOnType(char userType);

    boolean blockCustomer(User user) throws UnsupportedOperationException;

    boolean unBlockCustomer(User user) throws UnsupportedOperationException;


    /* Metodi che verranno usati SOLAMENTE nei cookie */
    User findLoggedUser();

    /* ********************************************** */
}
