package model.dao.mySQLJDBCImpl;

import model.dao.OrdersDAO;
import model.mo.ExtendedProduct;
import model.mo.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrdersDAOMySQLJDBCImpl implements OrdersDAO {

    /*
    questa è la classe che implementa a tutti gli effetti i metodi presenti nella OrdersDao
    dunque è l'implementazione per solamente MySQL, se si ci fossero stati altri DB con cui dialogare
    si sarebbero dovuti creare altri package dal nome per esempio PostgreeSQLJDBCImpl e avremmo dovuto
    scrivere le implementazioni per ogni possibile db
     */

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public OrdersDAOMySQLJDBCImpl (Connection connection) { this.connection = connection; }

    @Override
    public ArrayList<Orders> fetchOrdersByCustomerId(Long id) {

        /**
         * Con questo metodo e' possibile elencare tutti gli ordini eseguiti da un cliente in base al suo id
         */

        ArrayList<Orders> listOrders = new ArrayList<>();

        query = "SELECT * FROM ORDERS WHERE ID_CUSTOMER = ? AND DELETED = 0;";

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
                listOrders.add(readOrders(rs));
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
            System.err.println("Errore nella rs.getDate(\"INSERT_DATE\")");
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
            extendedProduct.setQuantity(rs.getInt("P.QUANTITY"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"P.QUANTITY\")");
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
        }  catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }

        /* Setto gli attributi esclusivi di ExtendedProduct */

        try {
            extendedProduct.setRequiredQuantity(rs.getInt("IL.QUANTITY"));
        }  catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"IL.QUANTITY\")");
            throw new RuntimeException(e);
        }

        return extendedProduct;
    }

    private Orders readOrders(ResultSet rs) {

        /**
         * Con questo metodo si costruisce l'oggetto Orders contenente tutti i dati raccolti nel db
         */

        Orders order = new Orders();

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
            order.setSellDate(rs.getObject("ORDER_DATE", LocalDate.class));
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
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }

        return order;
    }

}