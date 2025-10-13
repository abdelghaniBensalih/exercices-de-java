package app;

import data.Categorie;
import data.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe RapportService
 *
 * @author abdel
 */
class RapportServiceTest {

  private RapportService rapportService;
  private List<Ticket> tickets;
  private Serveur serveur1;
  private Serveur serveur2;
  private Produit produit1;
  private Produit produit2;
  private Categorie categorie1;
  private Categorie categorie2;

  @BeforeEach
  void setUp() {
    // Initialisation des données de test
    serveur1 = new Serveur(1, "Dupont", "Marie", "marie@email.com", "0123456789");
    serveur2 = new Serveur(2, "Martin", "Pierre", "pierre@email.com", "0123456790");

    categorie1 = new Categorie(1, "Boissons", "Boissons chaudes et froides");
    categorie2 = new Categorie(2, "Plats", "Plats principaux");

    produit1 = new Produit(1, "Café", "Café expresso", new BigDecimal("2.50"), categorie1, 100);
    produit2 =
        new Produit(2, "Sandwich", "Sandwich jambon", new BigDecimal("5.80"), categorie2, 50);

    tickets = new ArrayList<>();
    rapportService = new RapportService(tickets);
  }

  @Test
  @DisplayName("Test service avec liste vide")
  void testServiceListeVide() {
    LocalDate date = LocalDate.now();
    RapportService.RapportQuotidienServeur rapport =
        rapportService.genererRapportQuotidienParServeur(date);

    assertEquals(date, rapport.getDate());
    assertTrue(rapport.getDetailsServeurs().isEmpty());
    assertEquals(BigDecimal.ZERO, rapport.getChiffreAffairesTotal());
    assertEquals(0, rapport.getNombreTicketsTotal());
  }

  @Test
  @DisplayName("Test génération rapport quotidien par serveur")
  void testGenererRapportQuotidienParServeur() {
    LocalDate date = LocalDate.now();

    // Création de tickets pour la date donnée
    Ticket ticket1 = creerTicketFerme(1, date.atStartOfDay(), serveur1);
    ticket1.ajouterLigne(produit1, 2); // 5.00
    ticket1.fermerTicket();

    Ticket ticket2 = creerTicketFerme(2, date.atTime(12, 0), serveur1);
    ticket2.ajouterLigne(produit2, 1); // 5.80
    ticket2.fermerTicket();

    Ticket ticket3 = creerTicketFerme(3, date.atTime(15, 0), serveur2);
    ticket3.ajouterLigne(produit1, 1); // 2.50
    ticket3.fermerTicket();

    tickets.add(ticket1);
    tickets.add(ticket2);
    tickets.add(ticket3);

    RapportService.RapportQuotidienServeur rapport =
        rapportService.genererRapportQuotidienParServeur(date);

    assertEquals(date, rapport.getDate());
    assertEquals(2, rapport.getDetailsServeurs().size());

    // Vérification des détails pour serveur1 (2 tickets)
    RapportService.DetailServeur detailServeur1 =
        rapport.getDetailsServeurs().stream()
            .filter(d -> d.getServeur().equals(serveur1))
            .findFirst()
            .orElse(null);

    assertNotNull(detailServeur1);
    assertEquals(2, detailServeur1.getNombreTickets());

    // Vérification des détails pour serveur2 (1 ticket)
    RapportService.DetailServeur detailServeur2 =
        rapport.getDetailsServeurs().stream()
            .filter(d -> d.getServeur().equals(serveur2))
            .findFirst()
            .orElse(null);

    assertNotNull(detailServeur2);
    assertEquals(1, detailServeur2.getNombreTickets());
  }

  @Test
  @DisplayName("Test serveurs les plus performants")
  void testServeursLePlusPerformants() {
    LocalDate dateDebut = LocalDate.now().minusDays(7);
    LocalDate dateFin = LocalDate.now();

    // Création de tickets avec différents montants
    Ticket ticket1 = creerTicketFerme(1, dateDebut.atStartOfDay(), serveur1);
    ticket1.ajouterLigne(produit2, 3); // 17.40
    ticket1.fermerTicket();

    Ticket ticket2 = creerTicketFerme(2, dateDebut.plusDays(1).atStartOfDay(), serveur2);
    ticket2.ajouterLigne(produit1, 2); // 5.00
    ticket2.fermerTicket();

    Ticket ticket3 = creerTicketFerme(3, dateDebut.plusDays(2).atStartOfDay(), serveur1);
    ticket3.ajouterLigne(produit1, 1); // 2.50
    ticket3.fermerTicket();

    tickets.add(ticket1);
    tickets.add(ticket2);
    tickets.add(ticket3);

    List<RapportService.PerformanceServeur> topServeurs =
        rapportService.getServeursLePlusPerformants(dateDebut, dateFin, 2);

    assertEquals(2, topServeurs.size());

    // Le serveur1 devrait être premier (plus gros CA)
    assertEquals(serveur1, topServeurs.get(0).getServeur());
    assertEquals(2, topServeurs.get(0).getNombreTickets());

    // Le serveur2 devrait être second
    assertEquals(serveur2, topServeurs.get(1).getServeur());
    assertEquals(1, topServeurs.get(1).getNombreTickets());
  }

  @Test
  @DisplayName("Test catégories les plus vendues par jour de semaine")
  void testCategoriesLePlusVenduesParJourSemaine() {
    LocalDate lundi = LocalDate.now().with(DayOfWeek.MONDAY);
    LocalDate mardi = lundi.plusDays(1);

    // Tickets du lundi avec plus de boissons
    Ticket ticketLundi1 = creerTicketFerme(1, lundi.atStartOfDay(), serveur1);
    ticketLundi1.ajouterLigne(produit1, 3); // 3 cafés
    ticketLundi1.fermerTicket();

    Ticket ticketLundi2 = creerTicketFerme(2, lundi.atTime(12, 0), serveur2);
    ticketLundi2.ajouterLigne(produit1, 2); // 2 cafés
    ticketLundi2.fermerTicket();

    // Tickets du mardi avec plus de plats
    Ticket ticketMardi = creerTicketFerme(3, mardi.atStartOfDay(), serveur1);
    ticketMardi.ajouterLigne(produit2, 2); // 2 sandwichs
    ticketMardi.fermerTicket();

    tickets.add(ticketLundi1);
    tickets.add(ticketLundi2);
    tickets.add(ticketMardi);

    Map<DayOfWeek, List<RapportService.VenteCategorie>> ventesParJour =
        rapportService.getCategoriesLePlusVenduesParJourSemaine(lundi, mardi);

    // Vérification du lundi
    List<RapportService.VenteCategorie> ventesLundi = ventesParJour.get(DayOfWeek.MONDAY);
    assertFalse(ventesLundi.isEmpty());

    RapportService.VenteCategorie topCategoryLundi = ventesLundi.get(0);
    assertEquals(categorie1, topCategoryLundi.getCategorie()); // Boissons
    assertEquals(5, topCategoryLundi.getQuantiteVendue()); // 3 + 2 cafés

    // Vérification du mardi
    List<RapportService.VenteCategorie> ventesMardi = ventesParJour.get(DayOfWeek.TUESDAY);
    assertFalse(ventesMardi.isEmpty());

    RapportService.VenteCategorie topCategoryMardi = ventesMardi.get(0);
    assertEquals(categorie2, topCategoryMardi.getCategorie()); // Plats
    assertEquals(2, topCategoryMardi.getQuantiteVendue()); // 2 sandwichs
  }

  @Test
  @DisplayName("Test ajout de ticket au service")
  void testAjouterTicket() {
    assertEquals(
        0,
        rapportService.genererRapportQuotidienParServeur(LocalDate.now()).getNombreTicketsTotal());

    Ticket ticket = creerTicketFerme(1, LocalDate.now().atStartOfDay(), serveur1);
    ticket.ajouterLigne(produit1, 1);
    ticket.fermerTicket();

    rapportService.ajouterTicket(ticket);

    assertEquals(
        1,
        rapportService.genererRapportQuotidienParServeur(LocalDate.now()).getNombreTicketsTotal());
  }

  @Test
  @DisplayName("Test ajout de ticket null")
  void testAjouterTicketNull() {
    int nombreTicketsAvant =
        rapportService.genererRapportQuotidienParServeur(LocalDate.now()).getNombreTicketsTotal();

    rapportService.ajouterTicket(null);

    assertEquals(
        nombreTicketsAvant,
        rapportService.genererRapportQuotidienParServeur(LocalDate.now()).getNombreTicketsTotal());
  }

  @Test
  @DisplayName("Test exclusion des tickets non fermés")
  void testExclusionTicketsNonFermes() {
    LocalDate date = LocalDate.now();

    // Ticket ouvert
    Ticket ticketOuvert = new Ticket(1, date.atStartOfDay(), serveur1);
    ticketOuvert.ajouterLigne(produit1, 1);
    // Ne pas fermer le ticket

    // Ticket annulé
    Ticket ticketAnnule = new Ticket(2, date.atTime(12, 0), serveur1);
    ticketAnnule.ajouterLigne(produit1, 1);
    ticketAnnule.annulerTicket();

    // Ticket fermé
    Ticket ticketFerme = creerTicketFerme(3, date.atTime(15, 0), serveur1);
    ticketFerme.ajouterLigne(produit1, 1);
    ticketFerme.fermerTicket();

    tickets.add(ticketOuvert);
    tickets.add(ticketAnnule);
    tickets.add(ticketFerme);

    RapportService.RapportQuotidienServeur rapport =
        rapportService.genererRapportQuotidienParServeur(date);

    // Seul le ticket fermé devrait être pris en compte
    assertEquals(1, rapport.getNombreTicketsTotal());
    assertEquals(1, rapport.getDetailsServeurs().size());
    assertEquals(1, rapport.getDetailsServeurs().get(0).getNombreTickets());
  }

  /** Méthode utilitaire pour créer un ticket fermé */
  private Ticket creerTicketFerme(int id, LocalDateTime dateHeure, Serveur serveur) {
    return new Ticket(id, dateHeure, serveur);
  }
}
