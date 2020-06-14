package model.dao.mySQLJDBCImpl;

import com.sun.istack.internal.Nullable;
import javafx.util.Pair;
import model.dao.OrdersDAO;
import model.dao.StatisticsDAO;
import model.mo.ExtendedProduct;
import model.mo.Order;
import model.mo.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StatisticsDAOMySQLJDBCImpl implements StatisticsDAO {

    private final String COUNTER_ID = "orderId";
    private Connection connection;
    private PreparedStatement ps;
    private String query;
    private ResultSet rs;

    public StatisticsDAOMySQLJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Pair<Double, Double> totalEarningsWithAndWithoutDiscount() {
        /**
         * This method return two key-value pairs: totalWithoutDiscount <==> key
         *                                         totalWithDiscount <==> value
         * */
        Pair<Double, Double> results;
        ArrayList<Double> tot_con_sconto_per_ordine = new ArrayList<Double>();
        ArrayList<Double> tot_senza_sconto_per_ordine = new ArrayList<Double>();

        /*SOLDI CHE SI SAREBBERO FATTI SENZA SCONTO vs SOLDI FATTI CON GLI SCONTI APPLICATI*/
        query =
                "SELECT O.ID AS ID_ORDINE,O.TOT_PRICE AS TOT_CON_SCONTO_PER_ORDINE,"
                        + "(SELECT SUM(IL.QUANTITY * P.PRICE)"
                        + "        FROM ITEMS_LIST IL,"
                        + "             PRODUCT P"
                        + "        WHERE O.ID = IL.ID_ORDER AND IL.ID_PRODUCT = P.ID"
                        + "        ORDER BY O.ID DESC"
                        + ") AS TOT_SENZA_SCONTO_PER_ORDINE "
                        + "FROM ORDERS O;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
//            ps.setString(i++, COUNTER_ID);
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

        try {
            while (rs.next()) {
                try {
                    tot_con_sconto_per_ordine.add(rs.getDouble("TOT_CON_SCONTO_PER_ORDINE"));
                } catch (SQLException e) {
                    System.err.println("Errore nella tot_con_sconto_per_ordine.add(rs.getDouble(\"TOT_CON_SCONTO_PER_ORDINE\"));");
                    throw new RuntimeException(e);
                }
                try {
                    tot_senza_sconto_per_ordine.add(rs.getDouble("TOT_SENZA_SCONTO_PER_ORDINE"));
                } catch (SQLException e) {
                    System.err.println("Errore nella tot_senza_sconto_per_ordine.add(rs.getDouble(\"TOT_SENZA_SCONTO_PER_ORDINE\"));");
                    throw new RuntimeException(e);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nella while (rs.next())");
            throw new RuntimeException(e);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            System.err.println("Errore nella rs.close();");
            throw new RuntimeException(e);

        }

        try {
            ps.close();
        } catch (SQLException e) {
            System.err.println("Errore nella ps.close()");
            throw new RuntimeException(e);
        }

        /* calcolo il totale di ciascuna lista a la ritorno come oggetto coppia */
        Double tot_con_sconto = (double) 0;
        Double tot_senza_sconto = (double) 0;
        for(Double d : tot_con_sconto_per_ordine){
            tot_con_sconto += d;
        }
        for(Double d : tot_senza_sconto_per_ordine){
            tot_senza_sconto += d;
        }

        results = new Pair<Double,Double>(tot_senza_sconto,tot_con_sconto);

        return results;
    }



}