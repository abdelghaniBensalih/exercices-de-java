package data;

import data.Categorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Categorie
 *
 * @author abdel
 */
class CategorieTest {

  private Categorie categorie;

  @BeforeEach
  void setUp() {
    categorie = new Categorie(1, "Boissons", "Boissons chaudes et froides");
  }

  @Test
  @DisplayName("Test création d'une catégorie")
  void testCreationCategorie() {
    assertEquals(1, categorie.getId());
    assertEquals("Boissons", categorie.getNom());
    assertEquals("Boissons chaudes et froides", categorie.getDescription());
  }

  @Test
  @DisplayName("Test constructeur par défaut")
  void testConstructeurParDefaut() {
    Categorie categorieVide = new Categorie();
    assertEquals(0, categorieVide.getId());
    assertNull(categorieVide.getNom());
    assertNull(categorieVide.getDescription());
  }

  @Test
  @DisplayName("Test setters")
  void testSetters() {
    categorie.setId(2);
    categorie.setNom("Plats");
    categorie.setDescription("Plats principaux");

    assertEquals(2, categorie.getId());
    assertEquals("Plats", categorie.getNom());
    assertEquals("Plats principaux", categorie.getDescription());
  }

  @Test
  @DisplayName("Test equals et hashCode")
  void testEqualsEtHashCode() {
    Categorie categorie2 = new Categorie(1, "Autre", "Autre description");
    Categorie categorie3 = new Categorie(2, "Boissons", "Boissons chaudes et froides");

    // Même ID = égaux
    assertEquals(categorie, categorie2);
    assertEquals(categorie.hashCode(), categorie2.hashCode());

    // ID différent = différents
    assertNotEquals(categorie, categorie3);
    assertNotEquals(categorie.hashCode(), categorie3.hashCode());
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    String toString = categorie.toString();
    assertTrue(toString.contains("Boissons"));
    assertTrue(toString.contains("Boissons chaudes et froides"));
    assertTrue(toString.contains("1"));
  }

  @Test
  @DisplayName("Test avec valeurs nulles")
  void testValeursNulles() {
    categorie.setNom(null);
    categorie.setDescription(null);

    assertNull(categorie.getNom());
    assertNull(categorie.getDescription());
  }

  @Test
  @DisplayName("Test avec chaînes vides")
  void testChainesVides() {
    categorie.setNom("");
    categorie.setDescription("");

    assertEquals("", categorie.getNom());
    assertEquals("", categorie.getDescription());
  }
}
