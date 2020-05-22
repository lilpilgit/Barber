package common;

public class StaticFunc {

    private StaticFunc() {
    }

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
