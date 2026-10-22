package ma.projet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String[] MOIS = {"Janvier", "Février", "Mars", "Avril", "Mai",
        "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

    private static final SimpleDateFormat COURT = new SimpleDateFormat("dd/MM/yyyy");

    private DateUtil() {
    }

    public static Date date(String jour, String mois, String annee) {
        try {
            return COURT.parse(jour + "/" + mois + "/" + annee);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date invalide : " + jour + "/" + mois + "/" + annee, e);
        }
    }

    public static String court(Date date) {
        return date == null ? "" : COURT.format(date);
    }

    public static String longFormat(Date date) {
        if (date == null) {
            return "";
        }
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.DAY_OF_MONTH) + " "
                + MOIS[c.get(java.util.Calendar.MONTH)] + " " + c.get(java.util.Calendar.YEAR);
    }
}
