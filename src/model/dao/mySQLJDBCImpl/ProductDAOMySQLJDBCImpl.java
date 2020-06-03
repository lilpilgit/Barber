package model.dao.mySQLJDBCImpl;

import model.dao.ProductDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProductDAOMySQLJDBCImpl implements ProductDAO {

    /*
    questa è la classe che implementa a tutti gli effetti i metodi presenti nella ProductDao
    dunque è l'implementazione per solamente MySQL, se si ci fossero stati altri DB con cui dialogare
    si sarebbero dovuti creare altri package dal nome per esempio PostgreeSQLJDBCImpl e avremmo dovuto
    scrivere le implementazioni per ogni possibile db
 */
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public ProductDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean update(Product product) throws DuplicatedObjectException {
        /**
         * Update data about a product.
         *
         * @return Return the object updated correctly in the DB otherwise raise an exception.
         * */

        boolean exist; /* flag per sapere se esiste già un'utente con gli stessi dati */
        /* CON TALE QUERY CONTROLLO SE IL PRODOTTO ESISTE GIÀ CON LO STESSO NOME */

        query =
                "SELECT ID "
                        + "FROM PRODUCT "
                        + "WHERE DELETED = 0 AND NAME = ? AND ID <> ?;";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setString(i++, product.getName());
            ps.setLong(i++, product.getId());

        } catch (SQLException e) {
            System.err.println("Errore nella connection.prepareStatement");
            throw new RuntimeException(e);
        }

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Errore nella rs = ps.executeQuery()");
            throw new RuntimeException(e);
        }

        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire un altro prodotto con gli stessi dati!!!*/
        } catch (SQLException e) {
            System.err.println("Errore nella exist = rs.next();");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        if (exist) {
            /*NON È UN ERRORE BLOCCANTE ==> TODO: deve essere gestito a livello di controller dando un messaggio di errore all'utente*/
            throw new DuplicatedObjectException("ProductDAOJDBCImpl.update: Tentativo di aggiornamento di un prodotto già esistente con nome{" + product.getName() + "}.}.");
        }
        /*Se non è stata sollevata alcuna eccezione, allora possiamo aggiornare i dati */

        query
                = " UPDATE PRODUCT"
                + " SET "
                + "  ID_STRUCTURE = ?,"
                + "  PRICE = ?,"
                + "  PRODUCER = ?,"
                + "  DISCOUNT = ?,"
                + "  NAME = ?,"
                + "  INSERT_DATE = ?,"
                + "  PIC_NAME = ?,"
                + "  DESCRIPTION = ?,"
                + "  QUANTITY = ?,"
                + "  CATEGORY = ?,"
                + "  SHOWCASE = ?,"
                + "  DELETED = ?"
                + " WHERE"
                + "  ID = ?;";


        try {
            ps = connection.prepareStatement(query);
            int i = 1;
            ps.setLong(i++, product.getStructure().getId()); /* il prodotto ha una struttura di riferimento */
            ps.setBigDecimal(i++, product.getPrice());
            ps.setString(i++, product.getProducer());
            ps.setInt(i++, product.getDiscount());
            ps.setString(i++, product.getName());
            ps.setDate(i++, Date.valueOf(product.getInsertDate()));
            ps.setString(i++, product.getPictureName());
            ps.setString(i++, product.getDescription());
            ps.setInt(i++, product.getQuantity());
            ps.setString(i++, product.getCategory());
            ps.setBoolean(i++, product.inShowcase());
            ps.setBoolean(i++, product.isDeleted());
            ps.setLong(i++, product.getId());

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

        /*Chiudo il preparedStatement*/
        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /* se non è stata sollevata alcuna eccezione fin qui, ritorno true perché significa
         * che l'aggiornamento è andato a buon fine */
        return true;
    }

    @Override
    public boolean modifyShowcase(Product product, Boolean status) {

        query = "UPDATE PRODUCT SET SHOWCASE = ? WHERE ID = ?";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setBoolean(i++, status);
            ps.setLong(i++, product.getId());
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
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public ArrayList<Product> findShowcaseProduct() {
        ArrayList<Product> listProduct = new ArrayList<>();
        query = "SELECT * FROM PRODUCT WHERE SHOWCASE = 1;";
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
                listProduct.add(readProduct(rs));
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
        return listProduct;
    }

    @Override
    public Product findProductById(Long id) {
        Product product = new Product();
        String query = "SELECT * FROM PRODUCT WHERE ID = ?;";
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
            if (rs.next()) { //the element with this id is present
                product = readProduct(rs);
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
        return product;
    }

    /* TODO SISTEMARE SHOP.JSP ED AGGIORNARE CON METODO MVC */

    @Override
    /*
      Returns all product to sale ordered by Most Recent Date of Entry
     */
    public ArrayList<Product> findAllProducts() {
        ArrayList<Product> listProduct = new ArrayList<>();
        query = "SELECT * FROM PRODUCT ORDER BY INSERT_DATE DESC;";
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
                listProduct.add(readProduct(rs));
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
        return listProduct;
    }

    /* FUNZIONE AGGIORNATA CON METODO MVC */

    @Override
    public ArrayList<Product> fetchAllProducts() {

        ArrayList<Product> products = new ArrayList<>();
        /* Seleziono tutti i prodotti in ordine alfabetico per nome */
        query = "SELECT * FROM PRODUCT ORDER BY NAME;";

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
                products.add(readProduct(rs));
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
        return products;

    }

    @Override
    public ArrayList<String> findAllCategories() {
        ArrayList<String> listCategory = new ArrayList<>();
        query = "SELECT DISTINCT CATEGORY FROM PRODUCT ORDER BY CATEGORY;";
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
                listCategory.add(readCategory(rs));
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
        return listCategory;
    }

    @Override
    public ArrayList<String> findAllProducers() {
        ArrayList<String> listProducers = new ArrayList<>();
        query = "SELECT DISTINCT PRODUCER FROM PRODUCT ORDER BY PRODUCER;"; /*search producers order alphabetically from A to Z*/
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
                listProducers.add(readProducer(rs));
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
        return listProducers;
    }

    @Override
    public ArrayList<Product> findFilteredProducts(String category, String producer) {
        ArrayList<Product> listProduct = new ArrayList<>();
        query = "SELECT * FROM PRODUCT WHERE CATEGORY LIKE ? AND PRODUCER LIKE ? ORDER BY INSERT_DATE DESC;";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, category);
            ps.setString(2, producer);
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
                listProduct.add(readProduct(rs));
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
        return listProduct;
    }

    private String readCategory(ResultSet rs) {
        String category = "prova";
        try {
            category = rs.getString("CATEGORY");
        } catch (SQLException e) {
            System.err.println("Errore nella: category = rs.getString(\"CATEGORY\");");
            throw new RuntimeException(e);
        }

        return category;
    }

    private String readProducer(ResultSet rs) {
        String producer = "prova";
        try {
            producer = rs.getString("PRODUCER");
        } catch (SQLException e) {
            System.err.println("Errore nella: producer = rs.getString(\"PRODUCER\");");
            throw new RuntimeException(e);
        }

        return producer;
    }

    private Product readProduct(ResultSet rs) {
        Product product = new Product();
        try {
            product.setId(rs.getLong("ID"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"ID\")");
            throw new RuntimeException(e);
        }
        try {
            product.setProducer(rs.getString("PRODUCER"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PRODUCER\")");
            throw new RuntimeException(e);
        }
        try {
            product.setPrice(rs.getBigDecimal("PRICE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBigDecimal(\"PRICE\")");
            throw new RuntimeException(e);
        }
        try {
            product.setDiscount(rs.getInt("DISCOUNT"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getInt(\"DISCOUNT\")");
            throw new RuntimeException(e);
        }
        try {
            product.setName(rs.getString("NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"NAME\")");
            throw new RuntimeException(e);
        }
        try {
            product.setInsertDate(rs.getObject("INSERT_DATE", LocalDate.class));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getDate(\"INSERT_DATE\")");
            throw new RuntimeException(e);
        }
        try {
            product.setPictureName(rs.getString("PIC_NAME"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"PIC_NAME\")");
            throw new RuntimeException(e);
        }
        try {
            product.setDescription(rs.getString("DESCRIPTION"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"DESCRIPTION\")");
            throw new RuntimeException(e);
        }
        try {
            product.setQuantity(rs.getInt("QUANTITY"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getLong(\"QUANTITY\")");
            throw new RuntimeException(e);
        }
        try {
            product.setCategory(rs.getString("CATEGORY"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getString(\"CATEGORY\")");
            throw new RuntimeException(e);
        }
        try {
            product.setShowcase(rs.getBoolean("SHOWCASE"));
        } catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"SHOWCASE\")");
            throw new RuntimeException(e);
        }
        try {
            product.setDeleted(rs.getBoolean("DELETED"));
        }  catch (SQLException e) {
            System.err.println("Errore nella rs.getBoolean(\"DELETED\")");
            throw new RuntimeException(e);
        }

        return product;
    }
}
