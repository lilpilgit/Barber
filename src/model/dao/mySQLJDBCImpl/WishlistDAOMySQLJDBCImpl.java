package model.dao.mySQLJDBCImpl;

import model.dao.WishlistDAO;
import model.mo.ExtendedProduct;
import model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WishlistDAOMySQLJDBCImpl implements WishlistDAO {

    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public WishlistDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ArrayList<ExtendedProduct> fetchWishlist(User user) throws UnsupportedOperationException {
        /**
         * Fetch ArrayList<Product> that is Wishlist of user passed as parameter.
         *
         * @params User user : a user with type {C}
         * @return Return the ArrayList<Product> for the user passed as parameter
         * otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile caricare la wishlist di un utente che non è cliente. Errore con l'utente con id{" + user.getId() + "}.");

        ArrayList<ExtendedProduct> wishlist = new ArrayList<>();
        /* Seleziono i prodotti presenti nella wishlist dell'utente passato come parametro e verifico se questi sono o meno presenti nel carrello */
        query =
                "SELECT ID," +
                        "       PRODUCER," +
                        "       PRICE," +
                        "       DISCOUNT," +
                        "       NAME," +
                        "       PIC_NAME," +
                        "       DESCRIPTION," +
                        "       MAX_ORDER_QTY," +
                        "       CATEGORY," +
                        "       IF(EXISTS(SELECT C.ID_PRODUCT" +
                        "                 FROM CART C" +
                        "                 WHERE C.ID_CUSTOMER = W.ID_CUSTOMER" +
                        "                   AND C.ID_PRODUCT = W.ID_PRODUCT), TRUE, FALSE) IN_CART " +
                        "FROM WISHLIST W" +
                        "         INNER JOIN PRODUCT P ON W.ID_PRODUCT = P.ID " +
                        "WHERE W.ID_CUSTOMER = ?" +
                        "  AND P.DELETED = 0;";
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
            while (rs.next()) { /* Fin tanto che esiste un prodotto nella wishlist di tipo Product */
                wishlist.add(readWishlist(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore nella cart.add(readWishlist(rs));");
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

        return wishlist;

    }

    @Override
    public boolean addProductToWishlist(User user, Long idProduct) throws UnsupportedOperationException {
        /**
         * Add product to wishlist of user logged.
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to add to wishlist of user.
         *
         * @return true if product is successfully added to wishlist otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} al carrello dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query =
                "INSERT INTO WISHLIST(ID_CUSTOMER,ID_PRODUCT) VALUES(?,?);";

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

    @Override
    public boolean removeProductFromWishlist(User user, Long idProduct) throws UnsupportedOperationException {
        /**
         * Remove product from wishlist of logged user .
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of the product to remove from wishlist of user.
         *
         * @return true if product is successfully removed from wishlist otherwise raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile aggiungere il prodotto con id{" + idProduct + "} alla wishlist dell'utente con id{" + user.getId() + "} in quanto non è cliente.");


        query = "DELETE FROM WISHLIST WHERE ID_CUSTOMER = ? AND ID_PRODUCT = ?;";

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

    @Override
    public boolean inWishlist(User user,Long idProduct) throws UnsupportedOperationException{
        /**
         * Verify if product is in Wishlist of user passed as parameter.
         *
         * @params User user : a user with type {C}
         *         Long idProduct : id of product to search
         * @return Return true if there is in logged user's wishlist otherwise return false or raise an exception.
         * */

        /* Controllo se l'utente che mi è stato passato ha l'attributo type = 'C' */
        if (user.getType() != 'C')
            throw new UnsupportedOperationException("UserDAOMySQLJDBCImpl: Impossibile verificare se un prodotto è nella wishlist di un utente che non è cliente. Errore con l'utente con id{" + user.getId() + "}.");

        boolean inWishlist = false;
        /* Seleziono i prodotti presenti nella wishlist dell'utente passato come parametro */
        query =
                "SELECT W.ID_PRODUCT "
                        + "FROM WISHLIST W "
                        + "WHERE W.ID_CUSTOMER = ? AND W.ID_PRODUCT = ?;";
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
            if (rs.next()) { /* Esiste tale prodotto nella wishlist dell'utente */
                inWishlist = true;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella if (rs.next()){inWishlist = true;};");
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

        return inWishlist;

    }

    private ExtendedProduct readWishlist(ResultSet rs) {
        /**
         * Read a ExtendedProduct attributes for wishlist
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
            extendedProduct.setMaxOrderQuantity(rs.getInt("MAX_ORDER_QTY"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setMaxOrderQuantity(rs.getInt(\"MAX_ORDER_QTY\"));");
            throw new RuntimeException(e);
        }
        try {
            extendedProduct.setInCart(rs.getBoolean("IN_CART"));
        } catch (SQLException e) {
            System.err.println("Errore nella extendedProduct.setInCart(rs.getBoolean(\"IN_CART\"));");
            throw new RuntimeException(e);
        }

        return extendedProduct;
    }



}
