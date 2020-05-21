package model.dao;

import model.exception.DuplicatedObjectException;
import model.mo.User;

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

    User insert(Long id, String email, String name, String surname, String address,
                String phone, String password, Boolean isAdmin, Boolean isEmployee,
                Boolean isCustomer) throws DuplicatedObjectException;
}
