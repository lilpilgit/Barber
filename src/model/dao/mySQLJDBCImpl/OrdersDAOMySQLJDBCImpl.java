package model.dao.mySQLJDBCImpl;

import com.sun.istack.internal.Nullable;
import functions.StaticFunc;
import model.dao.OrdersDAO;
import model.mo.ExtendedProduct;
import model.mo.Order;
import model.mo.User;
import services.config.Configuration;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrdersDAOMySQLJDBCImpl implements OrdersDAO {

    /*
    questa è la classe che implementa a tutti gli effetti i metodi presenti nella OrdersDao
    dunque è l'implementazione per solamente MySQL, se si ci fossero stati altri DB con cui dialogare
    si sarebbero dovuti creare altri package dal nome per esempio PostgreeSQLJDBCImpl e avremmo dovuto
    scrivere le implementazioni per ogni possibile db
     */

    private final String COUNTER_ID = "orderId";
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public OrdersDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Order insert(User customer, BigDecimal totalPrice, ArrayList<ExtendedProduct> items) {
        /**
         * This method allows you to insert an order with correlated items list.
         * @params
         *              User customer: object of customer that do order
         *              BigDecimal totalPrice: total price of order calculated client-side
         *              ArrayList<ExtendendProduct> items: products to add to ITEMS_LIST into Database
         * @return Returns the order inserted correctly in the DB otherwise raises an exception
         * */
        Long newId = null;
        Order order = new Order();
        order.setCustomer(customer);
        order.setItemList(items);
        order.setOrderDate(LocalDate.now()); /* uso la data calcolata dal server */
//                                <%--0 = nothing-new--%>
//                                <%--25 = processing--%>
//                                <%--50 = sent--%>
//                                <%--75 = delivering--%>
//                                <%--100 = delivered--%>
//                                <%-- annulled --%>
//                                <%-- canceled --%> --%>
        order.setStatus(StaticFunc.NOTHING_NEW);
        order.setTotPrice(totalPrice);
        order.setShippingAddress(customer.getAddress());
        order.setDeleted(false); /* lo dobbiamo inserire !*/

        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO ORDINE DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER ORDINI DIVERSI SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/
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
            /* IL resultSet una volta letto l'id non serve più in quanto rimane da fare solo l'INSERT di ORDERS*/
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        /* L'unico campo che rimane da settare è l'ID. */
        order.setId(newId);

        query = "INSERT INTO ORDERS(ID, SELL_DATE, ORDER_DATE, STATUS, TOT_PRICE, SHIPPING_ADDR, DELETED, ID_CUSTOMER) VALUES(?,?,?,?,?,?,?,?);";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, order.getId());
            ps.setDate(i++, null);/* la data di vendita verrà settata solo una volta che l'ordine che sarà consegnato */
            ps.setDate(i++, Date.valueOf(order.getOrderDate()));
            ps.setString(i++, order.getStatus());
            ps.setBigDecimal(i++, order.getTotPrice());
            ps.setString(i++, order.getShippingAddress());
            ps.setBoolean(i++, order.isDeleted());
            ps.setLong(i++, order.getCustomer().getId());
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

        /* ora bisogna inserire una riga per ogni prodotto acquistato all'interno della tabella ITEMS_LIST */
        query = "INSERT INTO ITEMS_LIST(ID_PRODUCT, ID_ORDER, QUANTITY) VALUES(?,?,?);";
        for (ExtendedProduct item : order.getItemList()) {
            try {
                int i = 1;
                ps = connection.prepareStatement(query);
                ps.setLong(i++, item.getId());
                ps.setLong(i++, order.getId());
                ps.setInt(i++, item.getRequiredQuantity());
            } catch (SQLException e) {
                System.err.println("Errore nella connection.prepareStatement per l'item:" + item);
                throw new RuntimeException(e);
            }
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Errore nella ps.executeUpdate() per l'item:" + item);
                throw new RuntimeException(e);
            }
        }

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /*
         * Se non è stata sollevata alcuna eccezione fin qui, ritorno correttamente l'oggetto di classe Order
         * appena inserito
         * */

        return order;
    }

    @Override
    public boolean modifyStatusById(Long idOrder, String status, @Nullable LocalDate sellDate) {
        /**
         * Modify status. If status is >= 50 ( status greater than or equal to StaticFunc.SENT ) set SELL_DATE to date passed by client.
         * @param LocalDate sellDate : may be null if status is < 50
         * @return true if modified correctly otherwise raise exception
         */
        boolean yesAdditionalQuery = (sellDate != null);
        String addedQuery = (yesAdditionalQuery) ? "SELL_DATE = ?," : "";
        query
                = "UPDATE ORDERS"
                + " SET "
                + addedQuery
                + " STATUS = ? "
                + " WHERE ID = ?";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            if (yesAdditionalQuery)
                ps.setDate(i++, Date.valueOf(sellDate));
            ps.setString(i++, status);
            ps.setLong(i++, idOrder);
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
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close();");
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public ArrayList<Order> fetchOrdersByCustomerId(Long id) {

        /**
         * Fetch all orders by id of customer
         */

        ArrayList<Order> listOrders = new ArrayList<>();

        query = "SELECT * FROM ORDERS WHERE ID_CUSTOMER = ? AND DELETED = 0 ORDER BY ORDER_DATE DESC, ID DESC;";
        /* ORDINO SULLA DATA DECRESCENTE OVVERO PER ORDINI PIÙ RECENTI MA ANCHE SULL'ID CHE ESSENDO INCREMENTALE, PER ORDINI
         *  EFFETTUATI NELLA STESSA DATA MI CONSENTE DI VISUALIZZARLI ANCHE IN QUESTO CASO PER ORDINI PIÙ RECENTE */

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
            while (rs.next()) {
                listOrders.add(readOrder(rs));
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

        return listOrders;
    }

    @Override
    public ArrayList<Order> fetchAllOrdersForLogistics() {
        /**
         * Fetch all orders for logistics admin area.
         */

        ArrayList<Order> listOrders = new ArrayList<>();

        query =
                "SELECT ORDERS.ID,SELL_DATE,U.EMAIL,U.NAME,U.SURNAME,U.PHONE,STATUS "
                        + "FROM ORDERS INNER JOIN USER U on ORDERS.ID_CUSTOMER = U.ID ORDER BY ORDER_DATE DESC, ID DESC;";
        /* ORDINO SULLA DATA DECRESCENTE OVVERO PER ORDINI PIÙ RECENTI MA ANCHE SULL'ID CHE ESSENDO INCREMENTALE, PER ORDINI
         *  EFFETTUATI NELLA STESSA DATA MI CONSENTE DI VISUALIZZARLI ANCHE IN QUESTO CASO PER ORDINI PIÙ RECENTE */

        try {
            ps = connection.prepareStatement(query);
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
                listOrders.add(readOrderForLogistics(rs));
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

        return listOrders;
    }

    @Override
    public ArrayList<ExtendedProduct> listOrderedProducts(Long id) {
        /**
         * Con questo metodo e' possibile elencare tutti i prodotti che si trovano in un ordine in base
         * all'id dell'ordine
         */

        ArrayList<ExtendedProduct> listProducts = new ArrayList<>();

        query = "SELECT * FROM ITEMS_LIST IL INNER JOIN PRODUCT P ON ID_PRODUCT = ID WHERE ID_ORDER = ?;";

        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, id);
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
                listProducts.add(readListOrderedProducts(rs));
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

        return listProducts;
    }

    @Override
    public ArrayList<Order> fetchRangeOfOrdersForLogistics(Long offset) {
        /**
         * Fetch a range of orders for logistics admin area.
         */

        ArrayList<Order> listOrders = new ArrayList<>();

        query =
                "SELECT ORDERS.ID,SELL_DATE,U.EMAIL,U.NAME,U.SURNAME,U.PHONE,STATUS "
              + "FROM ORDERS INNER JOIN USER U on ORDERS.ID_CUSTOMER = U.ID "
              + "ORDER BY ORDER_DATE DESC, ID DESC "
              + "LIMIT ? OFFSET ? ";


        /* ORDINO SULLA DATA DECRESCENTE OVVERO PER ORDINI PIÙ RECENTI MA ANCHE SULL'ID CHE ESSENDO INCREMENTALE, PER ORDINI
         *  EFFETTUATI NELLA STESSA DATA MI CONSENTE DI VISUALIZZARLI ANCHE IN QUESTO CASO PER ORDINI PIÙ RECENTE */

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, Configuration.TOT_REC_TO_SHOW_LOGISTICS);
            ps.setLong(i++, offset);
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
                listOrders.add(readOrderForLogistics(rs));
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

        return listOrders;
    }

    @Override
    public Long countOrdersForLogistics() {
        /**
         * Count the number of orders done by customers.
         * @return Number of orders done by customers.
         */
        Long numberOfOrders = 0L;
        query = "SELECT COUNT(*) AS NUMBER_OF_ORDERS FROM ORDERS;";

        try {
            ps = connection.prepareStatement(query);
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
            if (rs.next()) {
                try {
                    numberOfOrders = rs.getLong("NUMBER_OF_ORDERS");
                } catch (SQLException e) {
                    System.err.println("Errore nella numberOfOrders = rs.getLong(\"NUMBER_OF_ORDERS\");");
                    throw new RuntimeException(e);
                }
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

        return numberOfOrders;
    }

    private ExtendedProduct readListOrderedProducts(ResultSet rs) {

        /**
         * Con questo metodo e' possibile costruire la lista di tutti i prodotti contenuti all'interno dell'ordine.
         * Viene utilizzata la classe ExtendedProduct in quanto e' necessario aggiungere a Product anche la quantità
         * scelta dal cliente
         */

        ExtendedProduct extendedProduct = new ExtendedProduct();

        /* Setto gli attributi standard di Product */

        try {
            extendedProduct.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setProducer(rs.getString("PRODUCER"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PRODUCER\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPrice(rs.getBigDecimal("PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBigDecimal(\"PRICE\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDiscount(rs.getInt("DISCOUNT"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getInt(\"DISCOUNT\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"NAME\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setInsertDate(rs.getObject("INSERT_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setInsertDate(rs.getObject(\"INSERT_DATE\", LocalDate.class));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPictureName(rs.getString("PIC_NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PIC_NAME\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDescription(rs.getString("DESCRIPTION"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"DESCRIPTION\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setMaxOrderQuantity(rs.getInt("MAX_ORDER_QTY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setMaxOrderQuantity(rs.getInt(\"MAX_ORDER_QTY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setCategory(rs.getString("CATEGORY"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"CATEGORY\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setShowcase(rs.getBoolean("SHOWCASE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"SHOWCASE\")");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDeleted(rs.getBoolean("DELETED"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }

        /* Setto gli attributi esclusivi di ExtendedProduct */

        try {
            extendedProduct.setRequiredQuantity(rs.getInt("QUANTITY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setRequiredQuantity(rs.getInt(\"QUANTITY\"));");
            throw new RuntimeException(e);
        }

        return extendedProduct;
    }

    private Order readOrderForLogistics(ResultSet rs) {

        /**
         * Read order's attributes with correlated customer's info for logistics admin area.
         */

        Order order = new Order();
        User customer = new User();
        order.setCustomer(customer);

        /* Setto gli attributi standard di Order */

        try {
            order.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.setId(rs.getLong(\"ID\"));");
            throw new RuntimeException(e);
        }
        try {
            order.setSellDate(rs.getObject("SELL_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella order.setSellDate(rs.getObject(\"SELL_DATE\", LocalDate.class));");
            throw new RuntimeException(e);
        }
        try {
            order.setStatus(rs.getString("STATUS"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.setStatus(rs.getString(\"STATUS\"));");
            throw new RuntimeException(e);
        }

        /* Setto gli attributi aggiuntivi provenienti da Customer */
        try {
            order.getCustomer().setEmail(rs.getString("EMAIL"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.getCustomer().setEmail(rs.getString(\"EMAIL\"));");
            throw new RuntimeException(e);
        }
        try {
            order.getCustomer().setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.getCustomer().setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            order.getCustomer().setSurname(rs.getString("SURNAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.getCustomer().setSurname(rs.getString(\"SURNAME\"));");
            throw new RuntimeException(e);
        }
        try {
            order.getCustomer().setPhone(rs.getString("PHONE"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.getCustomer().setPhone(rs.getString(\"PHONE\"));");
            throw new RuntimeException(e);
        }

        return order;
    }

    private Order readOrder(ResultSet rs) {

        /**
         * Con questo metodo si costruisce l'oggetto Order contenente tutti i dati raccolti nel db
         */

        Order order = new Order();
        User customer = new User();
        order.setCustomer(customer);

        try {
            order.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            order.setSellDate(rs.getObject("SELL_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"SELL_DATE\", LocalDate.class)");
            throw new RuntimeException(e);
        }
        try {
            order.setOrderDate(rs.getObject("ORDER_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getObject(\"ORDER_DATE\", LocalDate.class)");
            throw new RuntimeException(e);
        }
        try {
            order.setStatus(rs.getString("STATUS"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"STATUS\")");
            throw new RuntimeException(e);
        }
        try {
            order.setTotPrice(rs.getBigDecimal("TOT_PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBigDecimal(\"TOT_PRICE\"))");
            throw new RuntimeException(e);
        }
        try {
            order.setShippingAddress(rs.getString("SHIPPING_ADDR"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs..getString(\"SHIPPING_ADDR\")");
            throw new RuntimeException(e);
        }
        try {
            order.setDeleted(rs.getBoolean("DELETED"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }
        try {
            order.getCustomer().setId(rs.getLong("ID_CUSTOMER"));
        } catch (SQLException e) {
            System.err.println("Errore nella order.getCustomer().setId(rs.getLong(\"ID_CUSTOMER\"));");
            throw new RuntimeException(e);
        }

        return order;
    }

}