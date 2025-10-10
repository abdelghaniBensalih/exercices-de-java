# Parser de date simple

## But

Ce petit projet montre une méthode pour parser une date depuis une chaîne en Java.
La méthode supporte deux formats :

- "jj-mm-aaaa" (ex. 01-12-2020)
- "jj mm aaaa" (ex. 01 12 2020)

Elle vérifie :

- que le jour et le mois sont numériques et dans les bornes valides,
- que l'année est entre 1900 et 2100,
- les années bissextiles pour février.

## Fichiers importants

- `src/main/java/app/DateInvalideException.java` : exception personnalisée (messages en français).
- `src/main/java/app/DateUtils.java` : contient `public static Date parseDate(String str) throws DateInvalideException`.
- `src/main/java/app/Main.java` : petit programme de démonstration qui teste plusieurs cas.

## Exemples de sorties attendues

- `29-02-2020 -> 29-02-2020` (date valide)
- `29 02 2019 -> Erreur: Jour invalide: 29 (1-28)`
- `32-13-2025 -> Erreur: Jour invalide: 32 (1-31)` ou `Mois invalide` selon l'entrée
