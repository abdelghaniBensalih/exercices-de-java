package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Serveur
 *
 * @author abdel
 */
class ServeurTest {

  private Serveur serveur;

  @BeforeEach
  void setUp() {
    serveur = new Serveur(1, "Dupont", "Marie", "marie.dupont@email.com", "0123456789");
  }

  @Test
  @DisplayName("Test création d'un serveur")
  void testCreationServeur() {
    assertEquals(1, serveur.getId());
    assertEquals("Dupont", serveur.getNom());
    assertEquals("Marie", serveur.getPrenom());
    assertEquals("marie.dupont@email.com", serveur.getEmail());
    assertEquals("0123456789", serveur.getTelephone());
  }

  @Test
  @DisplayName("Test nom complet")
  void testNomComplet() {
    assertEquals("Marie Dupont", serveur.getNomComplet());
  }

  @Test
  @DisplayName("Test constructeur par défaut")
  void testConstructeurParDefaut() {
    Serveur serveurVide = new Serveur();
    assertEquals(0, serveurVide.getId());
    assertNull(serveurVide.getNom());
    assertNull(serveurVide.getPrenom());
  }

  @Test
  @DisplayName("Test setters")
  void testSetters() {
    serveur.setId(2);
    serveur.setNom("Martin");
    serveur.setPrenom("Pierre");
    serveur.setEmail("pierre.martin@email.com");
    serveur.setTelephone("0987654321");

    assertEquals(2, serveur.getId());
    assertEquals("Martin", serveur.getNom());
    assertEquals("Pierre", serveur.getPrenom());
    assertEquals("pierre.martin@email.com", serveur.getEmail());
    assertEquals("0987654321", serveur.getTelephone());
    assertEquals("Pierre Martin", serveur.getNomComplet());
  }

  @Test
  @DisplayName("Test equals et hashCode")
  void testEqualsEtHashCode() {
    Serveur serveur2 = new Serveur(1, "Autre", "Nom", "autre@email.com", "0000000000");
    Serveur serveur3 = new Serveur(2, "Dupont", "Marie", "marie.dupont@email.com", "0123456789");

    // Même ID = égaux
    assertEquals(serveur, serveur2);
    assertEquals(serveur.hashCode(), serveur2.hashCode());

    // ID différent = différents
    assertNotEquals(serveur, serveur3);
    assertNotEquals(serveur.hashCode(), serveur3.hashCode());
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    String toString = serveur.toString();
    assertTrue(toString.contains("Dupont"));
    assertTrue(toString.contains("Marie"));
    assertTrue(toString.contains("marie.dupont@email.com"));
    assertTrue(toString.contains("0123456789"));
  }
}
