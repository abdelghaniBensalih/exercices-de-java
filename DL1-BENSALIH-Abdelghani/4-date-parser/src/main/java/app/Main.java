package app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
  public static void main(String[] args) {
    String[] tests =
        new String[] {
          "32-13-2025", // jour et mois invalides
          "29-02-2020", // valide (bissextile)
          "13-12-2025", // date valide
          "29 02 2019", // invalide (2019 pas bissextile)
          "1-1-2000", // valide
          "15-08-1899", // annee hors plage
          "15-08-2101", // annee hors plage
          "a-b-c", // non numerique
          "31-04-2021" // 31 avril invalide
        };

    SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");

    for (String s : tests) {
      try {
        Date d = DateUtils.parseDate(s);
        System.out.println(s + " -> " + fmt.format(d));
      } catch (DateInvalideException e) {
        System.out.println(s + " -> Erreur: " + e.getMessage());
      }
    }
  }
}
