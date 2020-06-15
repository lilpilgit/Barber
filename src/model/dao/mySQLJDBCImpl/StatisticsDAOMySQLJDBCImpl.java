package model.dao.mySQLJDBCImpl;

import model.dao.StatisticsDAO;
import model.mo.Statistics;

import java.sql.*;

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
    public Statistics totalEarningsWithAndWithoutDiscount() {
        /**
         * This method return a <Statistic> object with all statistic field set.
         *
         * */
        Statistics statistics = new Statistics();

        /*
         SOLDI CHE SI SAREBBERO FATTI SENZA SCONTO vs SOLDI FATTI CON GLI SCONTI APPLICATI
         E GUADAGNO PERSO A CAUSA DEGLI SCONTI CALCOLATO COME DIFFERENZA
         */
        query =
                "SELECT "
              + "     SUM(TOT_WITH_DISCOUNT_FOR_ORDER) AS TOT_EARNINGS_WITH_DISCOUNT,"
              + "     SUM(TOT_WITHOUT_DISCOUNT_FOR_ORDER) AS TOT_EARNINGS_WITHOUT_DISCOUNT,"
              + "     SUM(TOT_WITHOUT_DISCOUNT_FOR_ORDER - TOT_WITH_DISCOUNT_FOR_ORDER) AS LOST_GAIN "
              + "FROM "
              + "     (SELECT"
              + "           O.ID AS ID_ORDINE,"
              + "           O.TOT_PRICE AS TOT_WITH_DISCOUNT_FOR_ORDER,"
              + "           (SELECT"
              + "                 SUM(IL.QUANTITY * P.PRICE)"
              + "            FROM"
              + "               ITEMS_LIST IL, PRODUCT P"
              + "            WHERE"
              + "               O.ID = IL.ID_ORDER"
              + "               AND IL.ID_PRODUCT = P.ID"
              + "            ORDER BY O.ID DESC) AS TOT_WITHOUT_DISCOUNT_FOR_ORDER"
              + "      FROM"
              + "          ORDERS O) AS TOTAL_FOR_ORDER;";

        try {
            ps = connection.prepareStatement(query);
            int i = 1;
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
                    statistics.setTotEarningsWithDiscount(rs.getDouble("TOT_EARNINGS_WITH_DISCOUNT"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statistics.setTotEarningsWithDiscount(rs.getDouble(\"TOT_EARNINGS_WITH_DISCOUNT\"));");
                    throw new RuntimeException(e);
                }
                try {
                    statistics.setTotEarningsWithoutDiscount(rs.getDouble("TOT_EARNINGS_WITHOUT_DISCOUNT"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statistics.setTotEarningsWithoutDiscount(rs.getDouble(\"TOT_EARNINGS_WITHOUT_DISCOUNT\"));");
                    throw new RuntimeException(e);
                }
                try {
                    statistics.setLostGain(rs.getDouble("LOST_GAIN"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statistics.setLostGain(rs.getDouble(\"LOST_GAIN\"));");
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

        return statistics;
    }



}