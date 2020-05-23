package model.dao;

import model.exception.NoCustomerCreatedException;
import model.mo.Customer;

import java.util.ArrayList;

public interface CustomerDAO {
    /*
     * questi metodi rappresentano la business logic per l'oggetto CUSTOMER
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe CUSTOMER
     */

    /*
     * Based on the SHOWCASE flag, it lists all the products that must be shown in the showcase
     *
     * @return all the products that must be shown in the showcase of the homepage
     */
    Customer insert(UserDAO userDAO,
                    String email, /* [+] attributo della table USER */
                    String name, /* [+] attributo della table USER */
                    String surname, /* [+] attributo della table USER */
                    String address, /* [+] attributo della table USER */
                    String phone, /* [+] attributo della table USER */
                    String password /* [+] attributo della table USER */) throws NoCustomerCreatedException;

    ArrayList<Customer> fetchAll();

    Customer findById(Long id);

    boolean blockCustomer(Customer customer);

    boolean unBlockCustomer(Customer customer);
}
