package data;

import data.Categorie;
import data.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Produit
 *
 * @author abdel
 */
class ProduitTest {

  private Produit produit;
  private Categorie categorie;

  @BeforeEach
  void setUp() {
    categorie = new Categorie(1, "Boissons", "Boissons chaudes et froides");
    produit = new Produit(1, "Café", "Café expresso", new BigDecimal("2.50"), categorie, 100);
  }

  @Test
  @DisplayName("Test création d'un produit")
  void testCreationProduit() {
    assertEquals(1, produit.getId());
    assertEquals("Café", produit.getNom());
    assertEquals("Café expresso", produit.getDescription());
    assertEquals(new BigDecimal("2.50"), produit.getPrix());
    assertEquals(categorie, produit.getCategorie());
    assertEquals(100, produit.getStock());
  }

  @Test
  @DisplayName("Test constructeur par défaut")
  void testConstructeurParDefaut() {
    Produit produitVide = new Produit();
    assertEquals(0, produitVide.getId());
    assertNull(produitVide.getNom());
    assertNull(produitVide.getDescription());
    assertNull(produitVide.getPrix());
    assertNull(produitVide.getCategorie());
    assertEquals(0, produitVide.getStock());
  }

  @Test
  @DisplayName("Test setters")
  void testSetters() {
    Categorie nouvelleCategorie = new Categorie(2, "Plats", "Plats principaux");

    produit.setId(2);
    produit.setNom("Thé");
    produit.setDescription("Thé vert");
    produit.setPrix(new BigDecimal("3.00"));
    produit.setCategorie(nouvelleCategorie);
    produit.setStock(50);

    assertEquals(2, produit.getId());
    assertEquals("Thé", produit.getNom());
    assertEquals("Thé vert", produit.getDescription());
    assertEquals(new BigDecimal("3.00"), produit.getPrix());
    assertEquals(nouvelleCategorie, produit.getCategorie());
    assertEquals(50, produit.getStock());
  }

  @Test
  @DisplayName("Test equals et hashCode")
  void testEqualsEtHashCode() {
    Produit produit2 =
        new Produit(1, "Autre", "Autre description", new BigDecimal("5.00"), categorie, 10);
    Produit produit3 =
        new Produit(2, "Café", "Café expresso", new BigDecimal("2.50"), categorie, 100);

    // Même ID = égaux
    assertEquals(produit, produit2);
    assertEquals(produit.hashCode(), produit2.hashCode());

    // ID différent = différents
    assertNotEquals(produit, produit3);
    assertNotEquals(produit.hashCode(), produit3.hashCode());
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    String toString = produit.toString();
    assertTrue(toString.contains("Café"));
    assertTrue(toString.contains("Café expresso"));
    assertTrue(toString.contains("2.5"));
    assertTrue(toString.contains("100"));
  }

  @Test
  @DisplayName("Test avec prix à zéro")
  void testPrixZero() {
    produit.setPrix(BigDecimal.ZERO);
    assertEquals(BigDecimal.ZERO, produit.getPrix());
  }

  @Test
  @DisplayName("Test avec stock négatif")
  void testStockNegatif() {
    produit.setStock(-5);
    assertEquals(-5, produit.getStock());
  }
}
