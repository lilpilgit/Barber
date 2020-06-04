package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.ExtendedProduct;
import model.mo.Structure;
import model.mo.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface UserDAO {

    /*
     * questi metodi rappresentano la business logic per l'oggetto USER
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe USER
     */

    /*
     * Based on the SHOWCASE flag, it lists all the products that must be shown in the showcase
     *
     * @return all the products that must be shown in the showcase of the homepage
     */

    User insert(Long id, Structure structure, String email, String name, String surname, String address,
                String phone, String password, LocalDate birthDate, String fiscalCode, char type) throws DuplicatedObjectException;

    boolean update(User user) throws DuplicatedObjectException;

    User findById(Long id);

    boolean delete(User user);

    User findByEmail(String email);

    ArrayList<User> fetchAllOnType(char userType);

    boolean blockCustomer(User user) throws UnsupportedOperationException;

    boolean unBlockCustomer(User user) throws UnsupportedOperationException;

    ArrayList<ExtendedProduct> fetchCart(User user) throws UnsupportedOperationException;

    boolean addProductToCart(User user, Long idProduct, Integer desiredQty) throws UnsupportedOperationException;

    /* Metodi che verranno usati SOLAMENTE nei cookie */
    User findLoggedUser();

    /* ********************************************** */
}
