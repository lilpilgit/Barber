package model.dao;

import com.sun.istack.internal.Nullable;
import model.mo.ExtendedProduct;
import model.mo.Order;
import model.mo.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public interface OrdersDAO {

    /*
     * questi metodi rappresentano la business logic per l'oggetto ORDERS
     * e verranno implementati da classi diverse in modo diverso a seconda del sorgente dati o di quale DB
     * è implementato nella applicazione. Pertanto fornisce solo una lista di quelli che sono i metodi che
     * devono essere implementati per poter accedere ai dati della classe ORDERS
     */

    Order insert(User customer, BigDecimal totalPrice, ArrayList<ExtendedProduct> items);
    boolean modifyStatusById(Long idOrder,String status,@Nullable LocalDate sellDate);
    ArrayList<Order> fetchAllOrdersForLogistics();
    ArrayList<Order> fetchOrdersByCustomerId(Long id);
    ArrayList<ExtendedProduct> listOrderedProducts(Long id);

}
