package app;


import java.util.Calendar;
import java.util.Date;

/**
 * Utilitaires pour parser une date depuis une chaine.
 * Accepté : "jj-mm-aaaa" ou "jj mm aaaa" (séparateur tiret ou espace).
 */
public class DateUtils {

    /**
     * Parse la chaîne str en Date.
     * @param str la chaine représentant la date
     * @return java.util.Date
     * @throws DateInvalideException si format invalide ou jour/mois/annee invalides
     */
    public static Date parseDate(String str) throws DateInvalideException {
        if (str == null) {
            throw new DateInvalideException("Chaîne nulle");
        }

        // Normaliser : remplacer les espaces multiples par un seul espace
        String s = str.trim().replaceAll("\\s+", " ");

        String[] parts = null;
        if (s.contains("-")) {
            parts = s.split("-");
        } else if (s.contains(" ")) {
            parts = s.split(" ");
        } else {
            throw new DateInvalideException("Format invalide. Utiliser jj-mm-aaaa ou jj mm aaaa");
        }

        if (parts.length != 3) {
            throw new DateInvalideException("Format invalide. Il faut 3 parties: jour, mois, année");
        }

        int jour, mois, annee;
        try {
            jour = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new DateInvalideException("Jour invalide: '" + parts[0] + "'");
        }
        try {
            mois = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new DateInvalideException("Mois invalide: '" + parts[1] + "'");
        }
        try {
            annee = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new DateInvalideException("Année invalide: '" + parts[2] + "'");
        }

        if (annee < 1900 || annee > 2100) {
            throw new DateInvalideException("Année hors plage (1900-2100): " + annee);
        }

        if (mois < 1 || mois > 12) {
            throw new DateInvalideException("Mois invalide: " + mois);
        }

        int maxJours = joursDansMois(mois, annee);
        if (jour < 1 || jour > maxJours) {
            throw new DateInvalideException("Jour invalide: " + jour + " (1-" + maxJours + ")");
        }

        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.YEAR, annee);
        cal.set(Calendar.MONTH, mois - 1);
        cal.set(Calendar.DAY_OF_MONTH, jour);

        return cal.getTime();
    }

    private static int joursDansMois(int mois, int annee) {
        switch (mois) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (estBissextile(annee)) ? 29 : 28;
            default:
                return 0; 
        }
    }

    private static boolean estBissextile(int annee) {
        if (annee % 4 != 0) return false;
        if (annee % 100 != 0) return true;
        return annee % 400 == 0;
    }
}
