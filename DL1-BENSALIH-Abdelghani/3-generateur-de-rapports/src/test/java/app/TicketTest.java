package app;

import data.Categorie;
import data.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Ticket
 *
 * @author abdel
 */
class TicketTest {

  private Ticket ticket;
  private Serveur serveur;
  private Produit produit1;
  private Produit produit2;
  private Categorie categorie;

  @BeforeEach
  void setUp() {
    // Initialisation des objets pour chaque test
    serveur = new Serveur(1, "Dupont", "Marie", "marie@email.com", "0123456789");
    categorie = new Categorie(1, "Boissons", "Boissons chaudes et froides");
    produit1 = new Produit(1, "Café", "Café expresso", new BigDecimal("2.50"), categorie, 100);
    produit2 = new Produit(2, "Thé", "Thé vert", new BigDecimal("2.80"), categorie, 80);

    ticket = new Ticket(1, LocalDateTime.now(), serveur);
  }

  @Test
  @DisplayName("Test création d'un ticket vide")
  void testCreationTicketVide() {
    assertEquals(1, ticket.getId());
    assertEquals(serveur, ticket.getServeur());
    assertEquals("OUVERT", ticket.getStatut());
    assertTrue(ticket.getLignes().isEmpty());
    assertNull(ticket.getMontantTotal());
  }

  @Test
  @DisplayName("Test ajout d'une ligne au ticket")
  void testAjouterLigne() {
    ticket.ajouterLigne(produit1, 2);

    assertEquals(1, ticket.getLignes().size());
    assertEquals(produit1, ticket.getLignes().get(0).getProduit());
    assertEquals(2, ticket.getLignes().get(0).getQuantite());
    assertEquals(new BigDecimal("5.00"), ticket.getMontantTotal());
  }

  @Test
  @DisplayName("Test ajout de plusieurs lignes")
  void testAjouterPlusieursLignes() {
    ticket.ajouterLigne(produit1, 2); // 2.50 * 2 = 5.00
    ticket.ajouterLigne(produit2, 1); // 2.80 * 1 = 2.80

    assertEquals(2, ticket.getLignes().size());
    assertEquals(new BigDecimal("7.80"), ticket.getMontantTotal());
  }

  @Test
  @DisplayName("Test suppression d'une ligne")
  void testSupprimerLigne() {
    ticket.ajouterLigne(produit1, 2);
    ticket.ajouterLigne(produit2, 1);

    ticket.supprimerLigne(0);

    assertEquals(1, ticket.getLignes().size());
    assertEquals(produit2, ticket.getLignes().get(0).getProduit());
    assertEquals(new BigDecimal("2.80"), ticket.getMontantTotal());
  }

  @Test
  @DisplayName("Test fermeture du ticket")
  void testFermerTicket() {
    ticket.ajouterLigne(produit1, 1);
    ticket.fermerTicket();

    assertEquals("FERME", ticket.getStatut());
    assertNotNull(ticket.getMontantTotal());
    assertNotNull(ticket.getMontantTTC());
  }

  @Test
  @DisplayName("Test annulation du ticket")
  void testAnnulerTicket() {
    ticket.ajouterLigne(produit1, 1);
    ticket.annulerTicket();

    assertEquals("ANNULE", ticket.getStatut());
  }

  @Test
  @DisplayName("Test calcul du montant TTC")
  void testCalculMontantTTC() {
    ticket.ajouterLigne(produit1, 1); // 2.50
    ticket.fermerTicket();

    BigDecimal montantHT = new BigDecimal("2.50");
    BigDecimal tva = montantHT.multiply(new BigDecimal("0.20")); // 20% TVA
    BigDecimal montantTTC = montantHT.add(tva);

    assertEquals(montantHT, ticket.getMontantTotal());
    assertEquals(montantTTC, ticket.getMontantTTC());
  }

  @Test
  @DisplayName("Test application d'une promotion")
  void testAppliquerPromo() {
    Promo promo =
        new Promo(
            1,
            "Réduction 10%",
            "Réduction de 10%",
            new BigDecimal("10"),
            LocalDateTime.now().toLocalDate(),
            LocalDateTime.now().toLocalDate().plusDays(1),
            true);

    ticket.ajouterLigne(produit1, 4); // 2.50 * 4 = 10.00
    ticket.appliquerPromo(promo);
    ticket.fermerTicket();

    // 10.00 - 10% = 9.00
    assertEquals(new BigDecimal("9.00"), ticket.getMontantTotal());
  }

  @Test
  @DisplayName("Test suppression d'une ligne inexistante")
  void testSupprimerLigneInexistante() {
    ticket.ajouterLigne(produit1, 1);
    int nombreLignesAvant = ticket.getLignes().size();

    ticket.supprimerLigne(5); // Index inexistant

    assertEquals(nombreLignesAvant, ticket.getLignes().size());
  }
}
