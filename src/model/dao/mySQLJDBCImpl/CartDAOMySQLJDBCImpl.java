package model.dao.mySQLJDBCImpl;

import model.dao.CartDAO;
import model.dao.UserDAO;
import model.exception.DuplicatedObjectException;
import model.mo.ExtendedProduct;
import model.mo.Product;
import model.mo.Structure;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CartDAOMySQLJDBCImpl implements CartDAO {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public CartDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<ExtendedProduct> fetchCart(User user) throws UnsupportedOperationException {
        /**
         * Fetch ArrayList<ExtendedProduct> that is Cart of user passed as parameter.
         *
         * @params User user : a user with type {C}
         * @return Return the ArrayList<ExtendedProduct> for the user passed as parameter
         * otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile caricare il carrello di un utente che non è cliente. Errore con l'utente con id{" + user.getId() + "}.");

        ArrayList<ExtendedProduct> cart = new ArrayList<>();
        /* Seleziono i prodotti presenti nel carrello dell'utente passato come parametro */
        query =

                "SELECT ID, PRODUCER, PRICE, DISCOUNT, NAME, PIC_NAME, DESCRIPTION, QUANTITY, CATEGORY,DESIRED_QTY, IF(EXISTS (SELECT W.ID_PRODUCT FROM WISHLIST W WHERE W.ID_PRODUCT = C.ID_PRODUCT),TRUE,FALSE) IN_WISHLIST "
                        + "FROM (CART C INNER JOIN PRODUCT P ON C.ID_PRODUCT = P.ID) "
                        + "WHERE C.ID_CUSTOMER = ? AND P.DELETED = 0";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            while (rs.next()) { /* Fin tanto che esiste un prodotto nel carrello di tipo ExtendedProduct */
                cart.add(readCart(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella cart.add(readCartProduct(rs));");
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

        return cart;

    }

    public boolean addProductToCart(User user, Long idProduct, Integer desiredQty) throws UnsupportedOperationException {
        /**
         * Add product to cart of user logged.
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to add to cart of user.
         *         Integer desiredQty : desired quantity to add to the cart
         * @return true if product is successfully added to cart otherwise raise an exception.
         * */

        ExtendedProduct productInCart = null; /* mappa la tupla del prodotto già esistente nel carrello dell'utente */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} al carrello dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query =
                "SELECT * "
                        + "FROM CART "
                        + "WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setLong(i++, idProduct);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
            throw new RuntimeException(e);
        }
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            if (rs.next()) {
                /*Se true significa che esiste già tale prodotto nel carrello dell'utente dunque va aggiornata solamente la DESIRED_QTY */
                query =
                        "UPDATE CART "
                                + "SET DESIRED_QTY = DESIRED_QTY + ? "
                                + "WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

                try {
                    int i = 1;
                    ps = connection.prepareStatement(query);
                    ps.setInt(i++, desiredQty);
                    ps.setLong(i++, user.getId());
                    ps.setLong(i++, idProduct);
                } catch (SQLException e) {
                    System.err.println("Errore nella ps = connection.prepareStatement(query)");
                    throw new RuntimeException(e);
                }
                try {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Errore nella ps.executeUpdate();");
                    throw new RuntimeException(e);
                }

            } else {
                /* il prodotto non esiste ancora nel carrello dell'utente pertanto bisogna aggiungerlo */
                query = "INSERT INTO CART(ID_CUSTOMER, ID_PRODUCT, DESIRED_QTY) VALUES(?,?,?);";
                try {
                    int i = 1;
                    ps = connection.prepareStatement(query);
                    ps.setLong(i++, user.getId());
                    ps.setLong(i++, idProduct);
                    ps.setInt(i++, desiredQty);
                } catch (SQLException e) {
                    System.err.println("Errore nella ps = connection.prepareStatement(query)");
                    throw new RuntimeException(e);
                }
                try {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Errore nella ps.executeUpdate();");
                    throw new RuntimeException(e);
                }

            }
        } catch (SQLException e) {
            System.err.println("Errore nella if(rs.next())");
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

        return true;
    }

    public boolean removeProductFromCart(User user, Long idProduct) throws UnsupportedOperationException {
        /**
         * Remove product from cart of logged user .
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to remove from cart of user.
         *
         * @return true if product is successfully removed from cart otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} al carrello dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query = "DELETE FROM CART WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, user.getId());
            ps.setLong(i++, idProduct);
        } catch (SQLException e) {
            System.err.println("Errore nella ps = connection.prepareStatement(query)");
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
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        return true;
    }

    private ExtendedProduct readCart(ResultSet rs) {
        /**
         * Read an ExtendedProduct attributes
         *
         * @return ExtendedProduct object read from result set.
         */

        ExtendedProduct extendedProduct = new ExtendedProduct();

        try {
            extendedProduct.setId(rs.getLong("ID"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella extendedProduct.setId(rs.getLong(\"ID\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setProducer(rs.getString("PRODUCER"));
        } catch (
                SQLException e) {
            System.err.println("Errore nella extendedProduct.setProducer(rs.getString(\"PRODUCER\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPrice(rs.getBigDecimal("PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setPrice(rs.getBigDecimal(\"PRICE\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDiscount(rs.getInt("DISCOUNT"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setDiscount(rs.getInt(\"DISCOUNT\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setName(rs.getString(\"NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setPictureName(rs.getString("PIC_NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setPictureName(rs.getString(\"PIC_NAME\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setDescription(rs.getString("DESCRIPTION"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setDescription(rs.getString(\"DESCRIPTION\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setCategory(rs.getString("CATEGORY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setCategory(rs.getString(\"CATEGORY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setQuantity(rs.getInt("QUANTITY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setQuantity(rs.getInt(\"QUANTITY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setRequiredQuantity(rs.getInt("DESIRED_QTY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setRequiredQuantity(rs.getInt(\"DESIRED_QTY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setInWishlist(rs.getBoolean("IN_WISHLIST"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setInWishlist(rs.getBoolean(\"IN_WISHLIST\"));");
            throw new RuntimeException(e);
        }

        return extendedProduct;
    }



}
