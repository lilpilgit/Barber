package functions;

public class StaticFunc {

    private StaticFunc() {
    }
//                                    <%--0 = nothing-new--%>
//                                <%--25 = processing--%>
//                                <%--50 = sent--%>
//                                <%--75 = delivering--%>
//                                <%--100 = delivered--%>
//                                <%-- canceled --%>
    public static final String NOTHING_NEW = "Nothing new yet...";
    public static final String PROCESSING = "Your order is being processed...";
    public static final String SENT = "Your order has been shipped...";
    public static final String DELIVERING = "Your order is being delivered...";
    public static final String DELIVERED = "Your order has been delivered.";
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
}
