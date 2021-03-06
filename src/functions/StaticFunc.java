package functions;

import java.math.BigDecimal;

public class StaticFunc {

    private StaticFunc() {
    }

    public static final String NOTHING_NEW = "Nothing new yet..."; //0
    public static final String PROCESSING = "Your order is being processed..."; //25
    public static final String SENT = "Your order has been shipped..."; //50
    public static final String DELIVERING = "Your order is being delivered..."; //75
    public static final String DELIVERED = "Your order has been delivered."; // 1000
    public static final String CANCELED = "Your order has been canceled.";

    public static String formatFinalAddress(String state, String region, String city, String street, String cap, String house_number) {
        /**
         * This function format ad address for this application.
         */
        String mandatory = state + "|" + region + "|" + city + "|" + cap + "|" + street;
        if (!house_number.equals(" "))
            mandatory = mandatory + "|" + house_number + "|"; /*!!! IMPORTANTE METTERE LA PIPE ALLA FINE ALTRIMENTI LO SPLIT NON FUNZIONA*/
        else
            mandatory = mandatory + "|" + " " + "|"; /* aggiungo comunque la | così quando devo splittare l'indirizzo mi ritorna stringa vuota*/
        return mandatory;
    }

    public static double round(double value, int precision) {
        if (precision < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
