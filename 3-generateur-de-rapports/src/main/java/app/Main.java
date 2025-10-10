package app;

import data.Categorie;
import data.Produit;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    List<Ticket> tickets = makeData();
    RapportService rs = new RapportService(tickets);
    System.out.println("Système de ventes\n");
    System.out.println("1. Rapport quotidien");
    LocalDate today = LocalDate.now();
    RapportService.RapportQuotidienServeur rapport = rs.genererRapportQuotidienParServeur(today);
    System.out.println(rapport);
    System.out.println("\n2. Top 3 des serveurs (7 jours)");
    LocalDate start = today.minusDays(7);
    List<RapportService.PerformanceServeur> top = rs.getServeursLePlusPerformants(start, today, 3);
    for (int i = 0; i < top.size(); i++) {
      System.out.println((i + 1) + ". " + top.get(i));
    }
    System.out.println("\n3. Catégories par jour de la semaine");
    Map<DayOfWeek, List<RapportService.VenteCategorie>> ventes =
        rs.getCategoriesLePlusVenduesParJourSemaine(start, today);
    for (DayOfWeek jour : DayOfWeek.values()) {
      System.out.println("\n" + jour + " :");
      List<RapportService.VenteCategorie> v = ventes.get(jour);
      if (v.isEmpty()) {
        System.out.println("  Aucune vente");
      } else {
        for (int i = 0; i < Math.min(3, v.size()); i++) {
          System.out.println("  " + (i + 1) + ". " + v.get(i));
        }
      }
    }
  }

  private static List<Ticket> makeData() {
    List<Ticket> tickets = new ArrayList<>();
    Categorie c1 = new Categorie(1, "Boissons", "Boissons chaudes et froides");
    Categorie c2 = new Categorie(2, "Plats", "Plats principaux");
    Categorie c3 = new Categorie(3, "Desserts", "Desserts et patisseries");
    Produit p1 = new Produit(1, "Cafe", "Cafe expresso", new BigDecimal("2.50"), c1, 100);
    Produit p2 =
        new Produit(2, "Sandwich", "Sandwich jambon-beurre", new BigDecimal("5.80"), c2, 50);
    Produit p3 = new Produit(3, "Tarte", "Tarte aux pommes", new BigDecimal("4.20"), c3, 30);
    Produit p4 = new Produit(4, "The", "The vert", new BigDecimal("2.80"), c1, 80);
    Produit p5 = new Produit(5, "Salade", "Salade composee", new BigDecimal("7.50"), c2, 40);
    Serveur s1 = new Serveur(1, "Dupont", "Marie", "marie.dupont@email.com", "0123456789");
    Serveur s2 = new Serveur(2, "Martin", "Pierre", "pierre.martin@email.com", "0123456790");
    Serveur s3 = new Serveur(3, "Bernard", "Sophie", "sophie.bernard@email.com", "0123456791");
    LocalDateTime now = LocalDateTime.now();
    for (int i = 0; i < 15; i++) {
      Ticket t = new Ticket(i + 1, now.minusHours(i % 8), i % 3 == 0 ? s1 : (i % 3 == 1 ? s2 : s3));
      if (i % 4 == 0) {
        t.ajouterLigne(p1, 2);
        t.ajouterLigne(p2, 1);
      } else if (i % 4 == 1) {
        t.ajouterLigne(p4, 1);
        t.ajouterLigne(p3, 1);
      } else if (i % 4 == 2) {
        t.ajouterLigne(p5, 1);
        t.ajouterLigne(p1, 1);
      } else {
        t.ajouterLigne(p2, 2);
        t.ajouterLigne(p4, 2);
        t.ajouterLigne(p3, 1);
      }
      t.fermerTicket();
      tickets.add(t);
    }
    for (int jour = 1; jour <= 7; jour++) {
      for (int i = 0; i < 8; i++) {
        Ticket t =
            new Ticket(
                100 + jour * 10 + i,
                now.minusDays(jour).minusHours(i % 6),
                i % 3 == 0 ? s1 : (i % 3 == 1 ? s2 : s3));
        DayOfWeek d = now.minusDays(jour).getDayOfWeek();
        if (d == DayOfWeek.MONDAY || d == DayOfWeek.TUESDAY) {
          t.ajouterLigne(p1, 2 + i % 3);
          t.ajouterLigne(p4, 1 + i % 2);
        } else if (d == DayOfWeek.WEDNESDAY || d == DayOfWeek.THURSDAY) {
          t.ajouterLigne(p2, 1 + i % 2);
          t.ajouterLigne(p5, i % 2);
          t.ajouterLigne(p1, 1);
        } else {
          t.ajouterLigne(p3, 1 + i % 2);
          t.ajouterLigne(p1, 1);
          t.ajouterLigne(p2, i % 2);
        }
        t.fermerTicket();
        tickets.add(t);
      }
    }
    return tickets;
  }
}
