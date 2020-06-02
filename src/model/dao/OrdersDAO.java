package model.dao;

import model.mo.ExtendedProduct;
import model.mo.Orders;

import java.util.ArrayList;

public interface OrdersDAO {

    /*
     * questi metodi rappresentano la business logic per l'oggetto ORDERS
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe ORDERS
     */

    ArrayList<Orders> fetchOrdersByCustomerId(Long id);
    ArrayList<ExtendedProduct> listOrderedProducts(Long id);

}
