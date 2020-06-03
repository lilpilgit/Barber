package model.dao.mySQLJDBCImpl;

import model.dao.ProductDAO;
import model.exception.DuplicatedObjectException;
import model.mo.Product;
import model.mo.Structure;

import java.math.BigDecimal;
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
    private final String COUNTER_ID = "productId";
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
    public boolean delete(Product product) {
        /**
         * Flag by DELETED column a PRODUCT as deleted
         *
         * @return true if delete go correctly otherwise raise exception
         */
        query
                = "UPDATE PRODUCT"
                + " SET DELETED = '1'"
                + " WHERE ID = ?";
        try {
            ps = connection.prepareStatement(query);
            int i = 1;
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
    public Product insert(Long id, String producer, BigDecimal price, Integer discount, String name, LocalDate insertDate,
                          String picName, String description, Integer quantity, String category, Structure structure) throws DuplicatedObjectException {
        /**
         * This method allows you to indifferently insert a product.
         * @params id parameter is DUMMY for DB insert but it's necessary when cookie is created ( in fact the method signature is the same)
         * @return Returns the product inserted correctly in the DB otherwise raises an exception
         * */

        /* Setto l'oggetto cui andro' a verificarne l'esistenza prima di inserirlo nel DB */
        Product product = new Product();
        product.setId(id);
        product.setProducer(producer);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setName(name);
        product.setInsertDate(insertDate);
        product.setPictureName(picName);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setStructure(structure);
        product.setShowcase(true);
        product.setDeleted(false);

        Long newId = null;

        /* Con tale query controllo se esistono gia' 2 prodotti con lo stesso nome */
        /* 2 prodotti con lo stesso nome non possono esistere nel db */

        query =
                "SELECT ID"
                        + " FROM PRODUCT"
                        + " WHERE NAME = ?;";

        System.err.println("NAME =>>" + product.getName());
        System.err.println("PRODUCER =>>" + product.getProducer());
        System.err.println("PRICE =>>" + product.getPrice());
        System.err.println("DISCOUNT =>>" + product.getDiscount());
        System.err.println("QUANTITY =>>" + product.getQuantity());



        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, product.getName());
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

        boolean exist; /* flag per sapere se esiste o meno */
        try {
            exist = rs.next(); /*se esiste almeno una riga non posso inserire altro!!!*/
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
            throw new DuplicatedObjectException("ProductDAOJDBCImpl.insert: Tentativo di inserimento di un prodotto già esistente con nome: {" + product.getName() + "}.");
        }

        /*LOCK SULL'OPERAZIONE DI AGGIORNAMENTO DELLA RIGA PERTANTO UNA QUALSIASI ALTRA TRANSAZIONE CHE PROVA AD AGGIUNGERE
         * UN NUOVO IMPIEGATO DEVE ASPETTARE CHE TALE TRANSAZIONE FINISCA E SONO SICURO CHE NON VERRÀ STACCATO 2 VOLTE LO STESSO
         * NUMERO PER IMPIEGATI DIVERSI SU CHIAMATE HTTP DIVERSE SU TRANSAZIONI DIVERSE*/
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
            /* IL resultSet una volta letto l'id non serve più in quanto rimane da fare solo l'INSERT di PRODUCT*/
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Errore nella rs.close();");
                throw new RuntimeException(e);
            }
        }

        /* L'unico campo che rimane da settare è l'ID. */
        product.setId(newId);


        query = "INSERT INTO PRODUCT(ID, PRODUCER, PRICE, DISCOUNT, NAME, INSERT_DATE, PIC_NAME, DESCRIPTION, QUANTITY, CATEGORY, SHOWCASE, DELETED, ID_STRUCTURE ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setLong(i++, product.getId());
            ps.setString(i++, product.getProducer());
            ps.setBigDecimal(i++, product.getPrice());
            ps.setInt(i++, product.getDiscount());
            ps.setString(i++, product.getName());
            ps.setDate(i++, Date.valueOf(product.getInsertDate()));
            ps.setString(i++, product.getPictureName());
            ps.setString(i++, product.getDescription());
            ps.setInt(i++, product.getQuantity());
            ps.setString(i++, product.getCategory());
            ps.setBoolean(i++, product.inShowcase());
            ps.setBoolean(i++, product.isDeleted());
            ps.setLong(i++, product.getStructure().getId());
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

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /*
         * Se non è stata sollevata alcuna eccezione fin qui, ritorno correttamente l'oggetto di classe User
         * appena inserito
         * */


        return product;
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
        query = "SELECT * FROM PRODUCT WHERE DELETED = '0' ORDER BY NAME;";

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
