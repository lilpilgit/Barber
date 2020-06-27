package model.dao.mySQLJDBCImpl;

import model.dao.StatisticsDAO;
import model.mo.StatisticsEarnings;

import java.sql.*;
import java.time.LocalDate;
import java.util.TreeMap;

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
    public StatisticsEarnings totalEarningsWithAndWithoutDiscount() {
        /**
         * This method return a <Statistic> object with the following fields set:
         * > totEarningsWithDiscount
         * > totEarningsWithoutDiscount
         * > lostGain
         * */
        StatisticsEarnings statisticsEarnings = new StatisticsEarnings();

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
                    statisticsEarnings.setTotEarningsWithDiscount(rs.getDouble("TOT_EARNINGS_WITH_DISCOUNT"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statisticsEarnings.setTotEarningsWithDiscount(rs.getDouble(\"TOT_EARNINGS_WITH_DISCOUNT\"));");
                    throw new RuntimeException(e);
                }
                try {
                    statisticsEarnings.setTotEarningsWithoutDiscount(rs.getDouble("TOT_EARNINGS_WITHOUT_DISCOUNT"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statisticsEarnings.setTotEarningsWithoutDiscount(rs.getDouble(\"TOT_EARNINGS_WITHOUT_DISCOUNT\"));");
                    throw new RuntimeException(e);
                }
                try {
                    statisticsEarnings.setLostGain(rs.getDouble("LOST_GAIN"));
                } catch (SQLException e) {
                    System.err.println("Errore nella statisticsEarnings.setLostGain(rs.getDouble(\"LOST_GAIN\"));");
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

        return statisticsEarnings;
    }

    @Override
    public TreeMap<Time,Integer> totalAppointmentGroupByHourStartInAYear(LocalDate date_for_year){
        /**
         * @param: <LocalDate> date_for_year : a date from which to extrapolate the year
         * This method return a TreeMap<Time,Integer> object with the following field set:
         *
         * > totAppointmentForHourStart
         * */
        TreeMap<Time,Integer> totAppointmentForHourStart = new TreeMap<Time,Integer>();

        query =
                "SELECT B.HOUR_START, COUNT(*) AS NUM_TOT_EFFETTUATI "
              + "FROM BOOKING B "
              + "WHERE YEAR(B.DATE) = YEAR(?) "
              + "GROUP BY B.HOUR_START "
              + "ORDER BY B.HOUR_START;";

        try {
            int i = 1;
            ps = connection.prepareStatement(query);
            ps.setDate(i++,Date.valueOf(date_for_year));
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
                    totAppointmentForHourStart.put(rs.getTime("HOUR_START"),rs.getInt("NUM_TOT_EFFETTUATI")) ;
                } catch (SQLException e) {
                    System.err.println("Errore nella totAppointmentForHourStart.put(rs.getTime(\"HOUR_START\"),rs.getInt(\"NUM_TOT_EFFETTUATI\"))");
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

        return totAppointmentForHourStart;
    }


}