package model.dao;

import model.exception.DuplicatedObjectException;
import model.exception.NoEmployeeCreatedException;
import model.mo.Employee;
import model.mo.Structure;

import java.time.LocalDate;
import java.util.ArrayList;


public interface EmployeeDAO {

    /*
     * questi metodi rappresentano la business logic per l'oggetto EMPLOYEE
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe EMPLOYEE
     */

    /*
     * Based on the SHOWCASE flag, it lists all the products that must be shown in the showcase
     *
     * @return all the products that must be shown in the showcase of the homepage
     */

    Employee insert(UserDAO userDAO,
                    LocalDate birthDate,
                    String fiscalCode,
                    LocalDate hireDate,
                    Structure structure,
                    String email,
                    String name,
                    String surname,
                    String address,
                    String phone,
                    String password) throws NoEmployeeCreatedException;
    boolean update(Employee employee) throws DuplicatedObjectException;

    ArrayList<Employee> fetchAll();

    Employee findById(Long id);
}
